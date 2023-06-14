package adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemWorkoutsBinding
import data.Workouts

class WorkoutsAdapter (
    private var workouts: List<Workouts>
    ) : RecyclerView.Adapter<WorkoutsAdapter.WorkoutsViewHolder>() {

    private lateinit var mListener : onItemClickListener
    interface  onItemClickListener {
        fun onItemClick(position: Int)
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
        return workouts.size
    }

    override fun onBindViewHolder(holder: WorkoutsViewHolder, position: Int) {
        holder.binding.apply {
            tvWorkouts.text = workouts[position].title
        }
    }

    inner class WorkoutsViewHolder(val binding: ItemWorkoutsBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition)
            }
        }
    }

}