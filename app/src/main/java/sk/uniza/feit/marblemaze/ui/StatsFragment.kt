package sk.uniza.feit.marblemaze.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import sk.uniza.feit.marblemaze.R
import sk.uniza.feit.marblemaze.data.AppDatabase
import sk.uniza.feit.marblemaze.data.DailyStat
import sk.uniza.feit.marblemaze.data.ResultDao

class StatsFragment : Fragment(R.layout.fragment_stats) {
    private val dao: ResultDao by lazy { AppDatabase.get(requireContext()).resultDao() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = view.findViewById<RecyclerView>(R.id.rv_stats)
        val empty = view.findViewById<TextView>(R.id.text_empty)
        viewLifecycleOwner.lifecycleScope.launch {
            val stats: List<DailyStat> = dao.getDailyStats()
            list.adapter = DailyStatsAdapter(stats)
            empty.visibility = if (stats.isEmpty()) View.VISIBLE else View.INVISIBLE
        }

        val btnBack = view.findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}