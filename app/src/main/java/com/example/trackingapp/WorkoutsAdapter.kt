package com.example.trackingapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemWorkoutsBinding

class WorkoutsAdapter (
    var workouts: List<Workouts>
    ) : RecyclerView.Adapter<WorkoutsAdapter.WorkoutsViewHolder>() {

    inner class WorkoutsViewHolder(val binding: ItemWorkoutsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWorkoutsBinding.inflate(layoutInflater, parent, false)
        return WorkoutsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return workouts.size
    }

    override fun onBindViewHolder(holder: WorkoutsViewHolder, position: Int) {
        holder.binding.apply {
            tvWorkout.text = workouts[position].title
        }
    }

}