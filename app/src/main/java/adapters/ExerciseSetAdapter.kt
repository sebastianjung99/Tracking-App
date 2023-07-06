package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemExerciseSetBinding
import data.ExerciseSet

class ExerciseSetAdapter(): RecyclerView.Adapter<ExerciseSetAdapter.SingleExerciseViewHolder>() {

    private val exerciseSetList = ArrayList<ExerciseSet>()

    private lateinit var mListener : onItemFocusChangeListener
    interface onItemFocusChangeListener {
        fun onItemFocusChange(position: Int,
              view: View,
              hasFocus: Boolean,
              exerciseSet: ExerciseSet
        )
    }
    fun setOnItemFocusChangeListener(listener: onItemFocusChangeListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleExerciseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExerciseSetBinding.inflate(layoutInflater, parent, false)
        return SingleExerciseViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: SingleExerciseViewHolder, position: Int) {
        holder.binding.apply {
            tvExerciseSetNumber.text = exerciseSetList[position].setNumber.toString()
            etExerciseSetReps.hint = exerciseSetList[position].repetitions.toString()
            etExerciseSetWeight.hint = exerciseSetList[position].weight.toString()
        }
    }

    override fun getItemCount(): Int {
        return exerciseSetList.size
    }

    fun setList(exerciseSets: List<ExerciseSet>) {
        exerciseSetList.clear()
        exerciseSetList.addAll(exerciseSets)
    }

    inner class SingleExerciseViewHolder(
        val binding: ItemExerciseSetBinding,
        listener: onItemFocusChangeListener
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.etExerciseSetReps.setOnFocusChangeListener { view, hasFocus ->
                listener.onItemFocusChange(
                    bindingAdapterPosition,
                    view,
                    hasFocus,
                    exerciseSetList[bindingAdapterPosition]
                )
            }
            binding.etExerciseSetWeight.setOnFocusChangeListener { view, hasFocus ->
                listener.onItemFocusChange(
                    bindingAdapterPosition,
                    view,
                    hasFocus,
                    exerciseSetList[bindingAdapterPosition]
                )
            }
        }
    }

}