package activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.R
import data.Workout
import adapters.WorkoutsAdapter
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.trackingapp.databinding.FragmentWorkoutsBinding
import com.google.android.material.snackbar.Snackbar
import data.TrackingAppDatabase
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
        val dao = TrackingAppDatabase.getInstance(requireActivity().application).workoutsDao()
        val factory = WorkoutsViewModelFactory(dao, 0)
        viewModel = ViewModelProvider(this, factory).get(WorkoutsViewModel::class.java)

        initRecyclerView()

        adapter.setOnItemClickListener(object : WorkoutsAdapter.onItemClickListener {
            override fun onItemClick(position: Int, view: View, workout: Workout) {
                when (view.id) {
                    binding.root.id -> {
                        val action = WorkoutsActivityDirections.actionWorkoutsToExercises(workout.workoutId)
                        findNavController().navigate(action)
                    }
                    R.id.btnEditWorkout -> {
                        singleWorkoutPopUpMenu(view, workout)
                    }
                }
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
            Workout(
                workoutId = 0,
                workoutTitle = binding.etAddWorkout.text.toString()
            )
        )
        binding.etAddWorkout.text.clear()
        requireActivity().hideKeyboard()
    }

    // TODO: remove workoutID from exercise_includedInWorkoutIDs
    private fun deleteWorkoutData(workouts_id: Int) {
        viewModel.deleteWorkout(workouts_id)
    }

    private fun updateWorkoutData(workout: Workout, newTitle: String) {
        viewModel.updateWorkout(
            Workout(
                workoutId = workout.workoutId,
                workoutTitle = newTitle
            )
        )
    }

    private fun singleWorkoutPopUpMenu(btnView: View, workout: Workout) {
        val popup = PopupMenu(binding.root.context, btnView)
        popup.menuInflater.inflate(R.menu.workouts_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.option_delete -> {
                    deleteWorkoutData(workout.workoutId)
                    true
                }
                R.id.option_edit -> {
                    // hide workout title and edit button, show edit Text and cancel+save button

                    val workoutsTextView: TextView =
                        binding.rvWorkouts.findContainingItemView(btnView)!!
                            .findViewById<TextView>(R.id.tvWorkouts)
                    val editWorkoutButton: ImageButton =
                        binding.rvWorkouts.findContainingItemView(btnView)!!
                            .findViewById<ImageButton>(R.id.btnEditWorkout)
                    val editWorkoutText: EditText =
                        binding.rvWorkouts.findContainingItemView(btnView)!!
                            .findViewById<EditText>(R.id.etWorkouts)
                    val editWorkoutSaveButton: ImageButton =
                        binding.rvWorkouts.findContainingItemView(btnView)!!
                            .findViewById<ImageButton>(R.id.btnEditWorkoutSave)
                    val editWorkoutCancelButton: ImageButton =
                        binding.rvWorkouts.findContainingItemView(btnView)!!
                            .findViewById<ImageButton>(R.id.btnEditWorkoutCancel)

                    // hide and show relevant elements
                    workoutsTextView.visibility = GONE
                    editWorkoutButton.visibility = GONE
                    editWorkoutText.visibility = VISIBLE
                    editWorkoutText.setText(workout.workoutTitle)
                    editWorkoutSaveButton.visibility = VISIBLE
                    editWorkoutCancelButton.visibility = VISIBLE

                    // set focus and show soft keyboard
                    editWorkoutText.requestFocus()
                    WindowCompat.getInsetsController(requireActivity().window, editWorkoutText).show(
                        WindowInsetsCompat.Type.ime())

                    editWorkoutSaveButton.setOnClickListener {
                        // check if empty
                        val title = editWorkoutText.text.toString()
                        if (title.trim().isEmpty()) {
                            // TODO: replace action with vector graphic?
                            Snackbar.make(
                                requireContext(),
                                binding.root,
                                getString(R.string.WorkoutTitleCannotBeEmpty),
                                Snackbar.LENGTH_SHORT
                            ).setAction("X"){}.show()
                        }
                        else {
                            // update database and hide editText and cancel+save button, show title and edit button
                            updateWorkoutData(workout, title)
                            workoutsTextView.visibility = VISIBLE
                            editWorkoutButton.visibility = VISIBLE
                            editWorkoutText.visibility = GONE
                            editWorkoutSaveButton.visibility = GONE
                            editWorkoutCancelButton.visibility = GONE

                            Toast.makeText(
                                requireContext(),
                                R.string.WorkoutUpdated,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                    editWorkoutCancelButton.setOnClickListener {
                        requireActivity().hideKeyboard()
                        // hide and show relevant elements
                        workoutsTextView.visibility = VISIBLE
                        editWorkoutButton.visibility = VISIBLE
                        editWorkoutText.visibility = GONE
                        editWorkoutSaveButton.visibility = GONE
                        editWorkoutCancelButton.visibility = GONE
                    }

                    true
                }
                else -> true
            }
        }
        popup.show()
    }


    private fun initRecyclerView() {
        adapter = WorkoutsAdapter()
        binding.rvWorkouts.adapter = adapter
        binding.rvWorkouts.layoutManager = LinearLayoutManager(requireContext())

        // display workouts list
        viewModel.workouts.observe(viewLifecycleOwner) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}