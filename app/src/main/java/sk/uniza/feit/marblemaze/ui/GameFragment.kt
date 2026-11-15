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
    private var calibrated = false
    private var neutralX = 0f
    private var neutralY = 0f

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
        calibrated = false
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        val x = event.values[0]; val y = event.values[1]
        val g = SensorManager.GRAVITY_EARTH
        if (!calibrated) {
            neutralX = x
            neutralY = y
            calibrated = true
        }
        val tx = (-(x - neutralX) / g * SENSITIVITY).coerceIn(-1f, 1f)
        val ty = ((y - neutralY) / g * SENSITIVITY).coerceIn(-1f, 1f)
        gameView.tilt = Vector2(tx, ty)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    companion object {
        const val SENSITIVITY = 2.5f
    }
}