package sk.uniza.feit.marblemaze.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import sk.uniza.feit.marblemaze.R
import sk.uniza.feit.marblemaze.data.AppDatabase
import sk.uniza.feit.marblemaze.data.ResultDao

class GameOverFragment : Fragment(R.layout.fragment_game_over) {
    private val dao: ResultDao by lazy { AppDatabase.get(requireContext()).resultDao() }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnMenu = view.findViewById<Button>(R.id.btn_menu)
        val btnStats = view.findViewById<Button>(R.id.btn_stats)
        val btnRetry = view.findViewById<Button>(R.id.btn_retry)

        val textDepth = view.findViewById<TextView>(R.id.text_levels_passed)
        val textBest = view.findViewById<TextView>(R.id.text_best_score)
        val textNewBest = view.findViewById<TextView>(R.id.text_new_best)

        val depth: Int = requireArguments().getInt(ARG_DEPTH)
        val prevBest: Int = requireArguments().getInt(ARG_PREV_BEST)

        textNewBest.visibility = if (depth > prevBest) View.VISIBLE else View.GONE
        textDepth.text = "%02d".format(depth)
        viewLifecycleOwner.lifecycleScope.launch {
            val best: Int = dao.getBest() ?: 0
            textBest.text = "%02d".format(best)
        }

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
        const val ARG_PREV_BEST = "prev_best"
    }
}