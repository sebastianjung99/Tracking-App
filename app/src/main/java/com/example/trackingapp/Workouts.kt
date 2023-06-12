package com.example.trackingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.trackingapp.databinding.FragmentWorkoutsBinding

/**
 * A simple [Fragment] subclass.
 */
class Workouts : Fragment() {

    var _binding: FragmentWorkoutsBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        binding.textView1.setOnClickListener {
            findNavController().navigate(R.id.action_workouts_to_weight)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}