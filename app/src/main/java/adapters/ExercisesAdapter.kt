package adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemExercisesBinding
import data.Exercises

class ExercisesAdapter (
    var exercises: List<Exercises>
    ) : RecyclerView.Adapter<ExercisesAdapter.WorkoutViewHolder>() {

    inner class WorkoutViewHolder(val binding: ItemExercisesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExercisesBinding.inflate(layoutInflater, parent, false)
        return WorkoutViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        holder.binding.apply {
            tvExercise.text = exercises[position].title
        }
    }

}