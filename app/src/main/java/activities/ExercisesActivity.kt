package activities

import adapters.ExercisesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentExercisesBinding
import com.google.android.material.snackbar.Snackbar
import data.Exercises
import data.ExercisesDatabase
import utils.Utils.hideKeyboard
import viewmodels.ExercisesViewModel
import viewmodels.ExercisesViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class ExercisesActivity : Fragment() {

    var _binding: FragmentExercisesBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: ExercisesViewModel
    private lateinit var adapter: ExercisesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        val dao = ExercisesDatabase.getInstance(requireActivity().application).exercisesDao()
        val factory = ExercisesViewModelFactory(dao)
        viewModel = ViewModelProvider(this, factory).get(ExercisesViewModel::class.java)

        initRecyclerView()

        adapter.setOnItemClickListener(object : ExercisesAdapter.onItemClickListener {
            override fun onItemClick(position: Int, view: View, exercises: Exercises) {
                when (view.id) {
                    binding.root.id -> {
                        findNavController().navigate(R.id.action_exercises_to_singleExercise)
                    }
                    R.id.btnEditExercise -> {
                        singleExercisePopupMenu(view, exercises)
                    }
                }
            }
        })

        binding.btnAddExercise.setOnClickListener {
            val title: String = binding.etAddExercise.text.toString()
            if (title.trim().isEmpty()) {
                // TODO: replace action with vector graphic?
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    getString(R.string.AddExerciseEmptyEditTextPrompt),
                    Snackbar.LENGTH_SHORT
                ).setAction("X"){}.show()
            }
            else {
                saveExerciseData()
            }
        }

        return binding.root
    }

    private fun saveExerciseData() {
        viewModel.insertExercise(
            Exercises(
                id = 0,
                title = binding.etAddExercise.text.toString()
            )
        )
        binding.etAddExercise.text.clear()
        requireActivity().hideKeyboard()
    }

    private fun deleteExercise(exercises_id: Int) {
        viewModel.deleteExercise(exercises_id)
    }

    private fun updateExercise(exercises: Exercises, newTitle: String) {
        viewModel.updateExercise(
            Exercises(
                id = exercises.id,
                title = newTitle
            )
        )
    }

    private fun initRecyclerView() {
        adapter = ExercisesAdapter()
        binding.rvExercises.adapter = adapter
        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())

        // display workouts list
        viewModel.exercises.observe(viewLifecycleOwner, {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun singleExercisePopupMenu(btnView: View, exercises: Exercises) {
        val popup = PopupMenu(binding.root.context, btnView)
        popup.menuInflater.inflate(R.menu.exercises_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.option_delete -> {
                    deleteExercise(exercises.id)
                    true
                }
                R.id.option_edit -> {
                    // hide exercise title and edit button, show edit Text and cancel+save button

                    val exerciseTextView: TextView =
                        binding.rvExercises.findContainingItemView(btnView)!!
                            .findViewById<TextView>(R.id.tvExercise)
                    val editExerciseButton: ImageButton =
                        binding.rvExercises.findContainingItemView(btnView)!!
                            .findViewById<ImageButton>(R.id.btnEditExercise)
                    val editExerciseEditText: EditText =
                        binding.rvExercises.findContainingItemView(btnView)!!
                            .findViewById<EditText>(R.id.etExercise)
                    val editExerciseSaveButton: ImageButton =
                        binding.rvExercises.findContainingItemView(btnView)!!
                            .findViewById<ImageButton>(R.id.btnEditExerciseSave)
                    val editExerciseCancelButton: ImageButton =
                        binding.rvExercises.findContainingItemView(btnView)!!
                            .findViewById<ImageButton>(R.id.btnEditExerciseCancel)

                    // hide and show relevant elements
                    exerciseTextView.visibility = View.GONE
                    editExerciseButton.visibility = View.GONE
                    editExerciseEditText.visibility = View.VISIBLE
                    editExerciseEditText.setText(exercises.title)
                    editExerciseSaveButton.visibility = View.VISIBLE
                    editExerciseCancelButton.visibility = View.VISIBLE

                    // set focus and show soft keyboard
                    editExerciseEditText.requestFocus()
                    WindowCompat.getInsetsController(requireActivity().window, editExerciseEditText).show(
                        WindowInsetsCompat.Type.ime())

                    editExerciseSaveButton.setOnClickListener {
                        // check if empty
                        val title = editExerciseEditText.text.toString()
                        if (title.trim().isEmpty()) {
                            // TODO: replace action with vector graphic?
                            Snackbar.make(
                                requireContext(),
                                binding.root,
                                getString(R.string.AddExerciseEmptyEditTextPrompt),
                                Snackbar.LENGTH_SHORT
                            ).setAction("X"){}.show()
                        }
                        else {
                            // update database and hide editText and cancel+save button, show title and edit button
                            updateExercise(exercises, title)
                            exerciseTextView.visibility = View.VISIBLE
                            editExerciseButton.visibility = View.VISIBLE
                            editExerciseEditText.visibility = View.GONE
                            editExerciseSaveButton.visibility = View.GONE
                            editExerciseCancelButton.visibility = View.GONE

                            Toast.makeText(
                                requireContext(),
                                R.string.ExerciseUpdated,
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }

                    editExerciseCancelButton.setOnClickListener {
                        requireActivity().hideKeyboard()
                        // hide and show relevant elements
                        exerciseTextView.visibility = View.VISIBLE
                        editExerciseButton.visibility = View.VISIBLE
                        editExerciseEditText.visibility = View.GONE
                        editExerciseSaveButton.visibility = View.GONE
                        editExerciseCancelButton.visibility = View.GONE
                    }

                    true
                }
                else -> true
            }
        }
        popup.show()
    }


}