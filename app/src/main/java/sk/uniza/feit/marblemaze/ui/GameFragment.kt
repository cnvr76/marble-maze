package sk.uniza.feit.marblemaze.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import sk.uniza.feit.marblemaze.R

class GameFragment : Fragment(R.layout.fragment_game) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLose = view.findViewById<Button>(R.id.btn_debug_lose)

        btnLose.setOnClickListener {
            findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment)
        }
    }
}