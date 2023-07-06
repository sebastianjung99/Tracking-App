package activities

import adapters.ExerciseSetAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.databinding.FragmentSingleExerciseBinding
import data.ExerciseSet
import data.TrackingAppDatabase
import viewmodels.WorkoutViewModel
import viewmodels.WorkoutViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class SingleExerciseActivity: Fragment() {

    val args: SingleExerciseActivityArgs by navArgs()

    var _binding: FragmentSingleExerciseBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: WorkoutViewModel
    private lateinit var currentSetsAdapter: ExerciseSetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSingleExerciseBinding.inflate(inflater, container, false)
        val dao = TrackingAppDatabase.getInstance(requireActivity().application).workoutDao()
        val factory = WorkoutViewModelFactory(dao, args.workoutId, args.exerciseId)
        viewModel = ViewModelProvider(this, factory).get(WorkoutViewModel::class.java)

        initRecyclerView()

        currentSetsAdapter.setOnItemFocusChangeListener(object: ExerciseSetAdapter.onItemFocusChangeListener {
            override fun onItemFocusChange(
                position: Int,
                view: View,
                hasFocus: Boolean,
                exerciseSet: ExerciseSet
            ) {
                // TODO: change to update database entry
                if (hasFocus) {
                    Toast.makeText(requireContext(), "has focus", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(requireContext(), "hasn't focus", Toast.LENGTH_SHORT).show()
                }
            }
        })

        return binding.root
    }

    private fun initRecyclerView() {
        currentSetsAdapter = ExerciseSetAdapter()
        binding.rvExerciseSets.adapter = currentSetsAdapter
        binding.rvExerciseSets.layoutManager = LinearLayoutManager(requireContext())

        viewModel.setsOfExerciseOfWorkoutToday.observe(viewLifecycleOwner) {
            currentSetsAdapter.setList(it)
            currentSetsAdapter.notifyDataSetChanged()
        }
    }

}