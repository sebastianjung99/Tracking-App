package adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemExerciseSetBinding
import data.ExerciseSet

class ExerciseSetAdapter(
    private val singleExercise: List<ExerciseSet>
): RecyclerView.Adapter<ExerciseSetAdapter.SingleExerciseViewHolder>() {

    inner class SingleExerciseViewHolder(val binding: ItemExerciseSetBinding): RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleExerciseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExerciseSetBinding.inflate(layoutInflater, parent, false)
        return SingleExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleExerciseViewHolder, position: Int) {
        holder.binding.apply {
            tvExerciseSet.text = "test"
        }
    }

    override fun getItemCount(): Int {
        return singleExercise.size
    }

}