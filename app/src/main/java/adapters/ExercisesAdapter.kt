package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemExercisesBinding
import data.Exercises

class ExercisesAdapter () : RecyclerView.Adapter<ExercisesAdapter.ExercisesViewHolder>() {

    private val exercisesList = ArrayList<Exercises>()

    private lateinit var mListener : onItemClickListener

    interface  onItemClickListener {
        fun onItemClick(position: Int, view: View, exercises: Exercises)
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
        return exercisesList.size
    }

    override fun onBindViewHolder(holder: ExercisesViewHolder, position: Int) {
        holder.binding.apply {
            tvExercise.text = exercisesList[position].title
        }
    }

    fun setList(exercises: List<Exercises>) {
        exercisesList.clear()
        exercisesList.addAll(exercises)
    }

    inner class ExercisesViewHolder(val binding: ItemExercisesBinding, listener: onItemClickListener): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, it, exercisesList[bindingAdapterPosition])
            }
            binding.btnEditExercise.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, it, exercisesList[bindingAdapterPosition])
            }
        }
    }

}