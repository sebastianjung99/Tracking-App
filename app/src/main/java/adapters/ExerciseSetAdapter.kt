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
        fun onItemFocusChange(
            position: Int,
            view: View,
            hasFocus: Boolean,
            exerciseSet: ExerciseSet
        )
    }
    fun setOnItemFocusChangeListener(listener: onItemFocusChangeListener) {
        mListener = listener
    }

    private lateinit var cListener: onItemClickListener
    interface  onItemClickListener {
        fun onItemClick(
            position: Int,
            view: View,
            exerciseSet: ExerciseSet
        )
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        cListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleExerciseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemExerciseSetBinding.inflate(layoutInflater, parent, false)
        return SingleExerciseViewHolder(binding, mListener, cListener)
    }

    override fun onBindViewHolder(holder: SingleExerciseViewHolder, position: Int) {
        holder.binding.apply {
            // setNumber is 2147483647 if dropset
            if (exerciseSetList[position].setNumber != Int.MAX_VALUE) {
                tvExerciseSetNumber.text = exerciseSetList[position].setNumber.toString()

                tvExerciseSetNumber.visibility = View.VISIBLE
                imageDropSetIndicator.visibility = View.GONE

                btnEditCurrentExerciseSet.visibility = View.VISIBLE
                btnDeleteDropSet.visibility = View.GONE
            }
            else {
                tvExerciseSetNumber.visibility = View.GONE
                imageDropSetIndicator.visibility = View.VISIBLE

                btnEditCurrentExerciseSet.visibility = View.GONE
                btnDeleteDropSet.visibility = View.VISIBLE
            }
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
        mListener: onItemFocusChangeListener,
        cListener: onItemClickListener
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            // always request focus elsewhere (e.g. binding.root.requestFocus()) when altering exerciseSetList
            // because app will crash if textEdit element with OnFocusChangeListener has focus
            // while recyclerview gets altered
            binding.etExerciseSetReps.setOnFocusChangeListener { view, hasFocus ->
                mListener.onItemFocusChange(
                    bindingAdapterPosition,
                    view,
                    hasFocus,
                    exerciseSetList[bindingAdapterPosition]
                )
            }
            binding.etExerciseSetWeight.setOnFocusChangeListener { view, hasFocus ->
                mListener.onItemFocusChange(
                    bindingAdapterPosition,
                    view,
                    hasFocus,
                    exerciseSetList[bindingAdapterPosition]
                )
            }
            binding.btnEditCurrentExerciseSet.setOnClickListener {
                cListener.onItemClick(
                    bindingAdapterPosition,
                    it,
                    exerciseSetList[bindingAdapterPosition]
                )
            }
            binding.btnDeleteDropSet.setOnClickListener {
                cListener.onItemClick(
                    bindingAdapterPosition,
                    it,
                    exerciseSetList[bindingAdapterPosition]
                )
            }
        }
    }

}