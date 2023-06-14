package activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.R
import data.Workouts
import adapters.WorkoutsAdapter
import com.example.trackingapp.databinding.FragmentWorkoutsBinding
import utils.Utils.hideKeyboard

/**
 * A simple [Fragment] subclass.
 */
class WorkoutsActivity : Fragment() {

    var _binding: FragmentWorkoutsBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)

        var workoutsList = mutableListOf(
            Workouts("Sample Workout"),
            Workouts("Another Sample Workout")
        )
        val adapter = WorkoutsAdapter(workoutsList)
        binding.rvWorkouts.adapter = adapter
        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAddWorkout.setOnClickListener {
            val title = binding.etAddWorkout.text.toString()
            val workout = Workouts(title)
            workoutsList.add(workout)
            adapter.notifyItemInserted(workoutsList.size - 1)

            binding.etAddWorkout.text.clear()
            requireActivity().hideKeyboard()
        }

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