package sk.uniza.feit.marblemaze.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import sk.uniza.feit.marblemaze.R

class MenuFragment : Fragment(R.layout.fragment_menu) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnPlay = view.findViewById<Button>(R.id.btn_play)
        btnPlay.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_gameFragment)
        }

        val btnStats = view.findViewById<Button>(R.id.btn_stats)
        btnStats.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_statsFragment)
        }
    }
}