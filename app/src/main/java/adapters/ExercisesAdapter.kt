package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemExercisesBinding
import data.Exercises

class ExercisesAdapter (
    var exercises: List<Exercises>
    ) : RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {

    inner class ExercisesViewHolder(val binding: ItemExercisesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExercisesBinding.inflate(layoutInflater, parent, false)
        return ExercisesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.binding.apply {
            val item = exercises[position]

            tvExercise.text = item.title

            rvSingleExercise.setHasFixedSize(true)
            rvSingleExercise.layoutManager = LinearLayoutManager(holder.itemView.context)

            val adapter = ExerciseSetAdapter(item.setsList)
            rvSingleExercise.adapter = adapter

            // expandable functionality
            val isExpandable = item.isExpendable
            rvSingleExercise.visibility = if (isExpandable) View.VISIBLE else View.GONE

            layoutSingleExercise.setOnClickListener {
                // reverse
                item.isExpendable = !item.isExpendable

                notifyItemChanged(position)
            }
        }
    }

}