package sk.uniza.feit.marblemaze.ui

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import sk.uniza.feit.marblemaze.R
import sk.uniza.feit.marblemaze.game.Vector2

class GameFragment : Fragment(R.layout.fragment_game), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var gameView: GameView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLose = view.findViewById<Button>(R.id.btn_debug_lose)
        sensorManager = requireContext().getSystemService(SensorManager::class.java)
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gameView = view.findViewById(R.id.game_view)

        btnLose.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        val values = event.values; val g = SensorManager.GRAVITY_EARTH
        gameView.tilt = Vector2(-values[0] / g, values[1] / g)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}