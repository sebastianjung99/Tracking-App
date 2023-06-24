package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemWorkoutsBinding
import data.Workout


class WorkoutsAdapter(
): RecyclerView.Adapter<WorkoutsAdapter.WorkoutsViewHolder>() {

    private val workoutList = ArrayList<Workout>()

    private lateinit var mListener : onItemClickListener
    interface  onItemClickListener {
        fun onItemClick(position: Int, view: View, workout: Workout)
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkoutsBinding.inflate(layoutInflater, parent, false)
        return WorkoutsViewHolder(binding, mListener)
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    override fun onBindViewHolder(holder: WorkoutsViewHolder, position: Int) {
        holder.binding.apply {
            tvWorkouts.text = workoutList[position].workoutTitle
        }
    }

    fun setList(workouts: List<Workout>) {
        workoutList.clear()
        workoutList.addAll(workouts)
    }

    inner class WorkoutsViewHolder(val binding: ItemWorkoutsBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, it, workoutList[bindingAdapterPosition])
            }

            binding.btnEditWorkout.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, it, workoutList[bindingAdapterPosition])
            }
        }
    }

}