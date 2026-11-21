package sk.uniza.feit.marblemaze.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import sk.uniza.feit.marblemaze.R
import sk.uniza.feit.marblemaze.data.DailyStat

class DailyStatsAdapter(val items: List<DailyStat>)
    : RecyclerView.Adapter<DailyStatsAdapter.Holder>() {

    class Holder(view: View): RecyclerView.ViewHolder(view) {
        val day = view.findViewById<TextView>(R.id.text_day)
        val best = view.findViewById<TextView>(R.id.text_best)
        val runs = view.findViewById<TextView>(R.id.text_runs)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_stats, parent, false)
        return Holder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.day.text = item.day
        holder.best.text = "%02d".format(item.best)
        holder.runs.text = item.runs.toString()
    }

    override fun getItemCount(): Int = items.size

}