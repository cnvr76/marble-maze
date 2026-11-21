package sk.uniza.feit.marblemaze.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import sk.uniza.feit.marblemaze.R

class GameOverFragment : Fragment(R.layout.fragment_game_over) {
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnMenu = view.findViewById<Button>(R.id.btn_menu)
        val btnStats = view.findViewById<Button>(R.id.btn_stats)
        val btnRetry = view.findViewById<Button>(R.id.btn_retry)

        val textDepth = view.findViewById<TextView>(R.id.text_levels_passed)

        val depth: Int = requireArguments().getInt(ARG_DEPTH)
        textDepth.text = "%02d".format(depth)

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

    companion object {
        const val ARG_DEPTH = "depth"
    }
}