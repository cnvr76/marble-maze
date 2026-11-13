package sk.uniza.feit.marblemaze.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import sk.uniza.feit.marblemaze.R

class StatsFragment : Fragment(R.layout.fragment_stats) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnBack = view.findViewById<Button>(R.id.btn_back)
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}