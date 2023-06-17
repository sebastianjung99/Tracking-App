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
import androidx.lifecycle.ViewModelProvider
import com.example.trackingapp.databinding.FragmentWorkoutsBinding
import com.google.android.material.snackbar.Snackbar
import data.WorkoutsDatabase
import utils.Utils.hideKeyboard
import viewmodels.WorkoutsViewModel
import viewmodels.WorkoutsViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class WorkoutsActivity : Fragment() {

    var _binding: FragmentWorkoutsBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: WorkoutsViewModel
    private lateinit var adapter: WorkoutsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       _binding = FragmentWorkoutsBinding.inflate(inflater, container, false)
        val dao = WorkoutsDatabase.getInstance(requireActivity().application).workoutsDao()
        val factory = WorkoutsViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(WorkoutsViewModel::class.java)

        initRecyclerView()

        adapter.setOnItemClickListener(object : WorkoutsAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                findNavController().navigate(R.id.action_workouts_to_exercises)
            }
        })

        binding.btnAddWorkout.setOnClickListener {
            val title = binding.etAddWorkout.text.toString()
            if (title.trim().isEmpty()) {
                // TODO: replace action with vector graphic?
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    getString(R.string.NewWorkoutEmptyEditTextPrompt),
                    Snackbar.LENGTH_SHORT
                ).setAction("X"){}.show()
            }
            else {
                saveWorkoutData()
            }
        }

        return binding.root
    }

    private fun saveWorkoutData() {
        viewModel.insertWorkout(
            Workouts(
                id = 0,
                title = binding.etAddWorkout.text.toString()
            )
        )
        binding.etAddWorkout.text.clear()
        requireActivity().hideKeyboard()
    }

    private fun initRecyclerView() {
        adapter = WorkoutsAdapter()
        binding.rvWorkouts.adapter = adapter
        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())

        // display workouts list
        viewModel.workouts.observe(viewLifecycleOwner, {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}