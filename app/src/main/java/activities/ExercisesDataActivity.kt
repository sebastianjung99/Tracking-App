package activities

import adapters.ExercisesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentExercisesDataBinding
import data.Exercise
import data.TrackingAppDatabase
import viewmodels.WorkoutViewModel
import viewmodels.WorkoutViewModelFactory

class ExercisesDataActivity: Fragment() {

    val args: ExercisesDataActivityArgs by navArgs()

    var _binding: FragmentExercisesDataBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: WorkoutViewModel
    private lateinit var adapter: ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesDataBinding.inflate(inflater, container, false)

        val workoutDao = TrackingAppDatabase.getInstance(requireActivity().application).workoutDao()
        val workoutFactory = WorkoutViewModelFactory(workoutDao, 0, 0)
        viewModel = ViewModelProvider(this, workoutFactory).get(WorkoutViewModel::class.java)

        binding.tvSelectedDataTitle.text = args.selectedData

        initRecyclerView()

        initItemClick()

        setSortButton()

        return binding.root
    }

    private fun initRecyclerView() {
        adapter = ExercisesAdapter()
        binding.rvSelectedData.adapter = adapter
        binding.rvSelectedData.layoutManager = LinearLayoutManager(requireContext())

        // add divider
        var dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        ResourcesCompat.getDrawable(resources, R.drawable.divider, null)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        binding.rvSelectedData.addItemDecoration(dividerItemDecoration)

        viewModel.allExercises.observe(viewLifecycleOwner) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initItemClick() {
        adapter.setOnItemClickListener(object: ExercisesAdapter.onItemClickListener {
            override fun onItemClick(position: Int, view: View, exercise: Exercise) {
                when (view.id) {
                    binding.root.id -> {
                        val action = ExercisesDataActivityDirections.actionExercisesDataFragmentToSingleExerciseData(
                            exercise.exerciseId
                        )
                        findNavController().navigate(action)
                    }
                    R.id.btnEditExercise -> {
                        // TODO: copy edit exercise button functionality from ExercisesActivity
                    }
                }
            }
        })
    }

    private fun setSortButton() {
        // TODO: implement sort button functionality
    }
}