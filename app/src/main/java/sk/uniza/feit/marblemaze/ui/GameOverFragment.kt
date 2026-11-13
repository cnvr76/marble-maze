package sk.uniza.feit.marblemaze.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import sk.uniza.feit.marblemaze.R

class GameOverFragment : Fragment(R.layout.fragment_game_over) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnMenu = view.findViewById<Button>(R.id.btn_menu)
        val btnStats = view.findViewById<Button>(R.id.btn_stats)
        val btnRetry = view.findViewById<Button>(R.id.btn_retry)

        btnMenu.setOnClickListener {
            findNavController().navigate(R.id.action_gameOverFragment_to_menuFragment)
        }

        btnStats.setOnClickListener {
            findNavController().navigate(R.id.action_gameOverFragment_to_statsFragment)
        }

        btnRetry.setOnClickListener {
            findNavController().navigate(R.id.action_gameOverFragment_to_gameFragment)
        }
    }
}