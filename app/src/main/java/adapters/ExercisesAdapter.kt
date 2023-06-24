package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemExercisesBinding
import data.Exercise

class ExercisesAdapter () : RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {

    private val exerciseList = ArrayList<Exercise>()

    private lateinit var mListener : onItemClickListener

    interface  onItemClickListener {
        fun onItemClick(position: Int, view: View, exercise: Exercise)
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercisesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExercisesBinding.inflate(layoutInflater, parent, false)
        return ExercisesViewHolder(binding, mListener)
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.binding.apply {
            tvExercise.text = exerciseList[position].exerciseTitle
        }
    }

    fun setList(workoutWithExercises: List<Exercise>) {
        exerciseList.clear()
        exerciseList.addAll(workoutWithExercises)
    }

    inner class ExercisesViewHolder(val binding: ItemExercisesBinding, listener: onItemClickListener): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, it, exerciseList[bindingAdapterPosition])
            }
            binding.btnEditExercise.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, it, exerciseList[bindingAdapterPosition])
            }
        }
    }

}