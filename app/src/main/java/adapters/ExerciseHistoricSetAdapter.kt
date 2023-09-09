package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemExerciseSetHistoricBinding
import data.ExerciseSet

class ExerciseHistoricSetAdapter(): RecyclerView.Adapter<ExerciseHistoricSetAdapter.SingleExerciseViewHolder>() {

    private val exerciseSetList = ArrayList<ExerciseSet>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleExerciseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExerciseSetHistoricBinding.inflate(layoutInflater, parent, false)
        return SingleExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SingleExerciseViewHolder, position: Int) {
        holder.binding.apply {
            // setNumber is 2147483647 if dropset
            if (exerciseSetList[position].setNumber != Int.MAX_VALUE) {
                tvExerciseSetNumber.text = exerciseSetList[position].setNumber.toString()
                tvExerciseSetNumber.visibility = View.VISIBLE
                imageDropSetIndicator.visibility = View.GONE
            }
            else {
                tvExerciseSetNumber.visibility = View.GONE
                imageDropSetIndicator.visibility = View.VISIBLE
            }
            tvExerciseSetReps.text = exerciseSetList[position].repetitions.toString()
            tvExerciseSetWeight.text = exerciseSetList[position].weight.toString()
        }
    }

    override fun getItemCount(): Int {
        return exerciseSetList.size
    }

    fun setList(exerciseSets: List<ExerciseSet>) {
        exerciseSetList.clear()
        exerciseSetList.addAll(exerciseSets)
    }

    inner class SingleExerciseViewHolder(val binding: ItemExerciseSetHistoricBinding): RecyclerView.ViewHolder(binding.root)

}