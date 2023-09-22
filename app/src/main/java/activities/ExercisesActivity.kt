package activities

import Other.Gesture
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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentExercisesBinding
import com.google.android.material.snackbar.Snackbar
import data.Exercise
import data.TrackingAppDatabase
import data.Workout
import data.relations.WorkoutExerciseCrossRef
import kotlinx.coroutines.launch
import utils.Utils.hideKeyboard
import viewmodels.WorkoutViewModel
import viewmodels.WorkoutViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class ExercisesActivity : Fragment() {

    val args: ExercisesActivityArgs by navArgs()

    var _binding: FragmentExercisesBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: WorkoutViewModel
    private lateinit var adapter: ExercisesAdapter

    private lateinit var workout: Workout

    private var nextPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)

        val workoutDao = TrackingAppDatabase.getInstance(requireActivity().application).workoutDao()
        val workoutFactory = WorkoutViewModelFactory(workoutDao, args.workoutId, 0)
        viewModel = ViewModelProvider(this, workoutFactory).get(WorkoutViewModel::class.java)

        // set workout title
        lifecycleScope.launch {
            workout = viewModel.getWorkoutById(args.workoutId)
            binding.tvWorkoutTitle.text = workout.workoutTitle
        }

        initRecyclerView()

        initGesture()

        adapter.setOnItemClickListener(object : ExercisesAdapter.onItemClickListener {
            override fun onItemClick(position: Int, view: View, exercise: Exercise) {
                when (view.id) {
                    binding.root.id -> {
                        val action = ExercisesActivityDirections.actionExercisesToSingleExercise(
                            exerciseId = exercise.exerciseId,
                            workoutId = args.workoutId
                        )
                        findNavController().navigate(action)
                    }
                    R.id.btnEditExercise -> {
                        singleExercisePopupMenu(view, exercise)
                    }
                }
            }
        })

        binding.btnExercisesBack.setOnClickListener {
            findNavController().navigate(ExercisesActivityDirections.actionExercisesToWorkouts())
        }

        binding.btnWorkoutOptions.setOnClickListener {
            exercisesPopUpMenu(it)
        }

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
        val title = binding.etAddExercise.text.toString()

        // TODO: check if exercise already exists, maybe something like this?:
        lifecycleScope.launch {
            val existingExercise = viewModel.getExerciseByTitle(title)

            // don't be fooled if IDE says expression is always true
            if (existingExercise != null) {
                viewModel.insertWorkoutExerciseCrossRef(
                    WorkoutExerciseCrossRef(
                        workout_id = args.workoutId,
                        exercise_id = existingExercise.exerciseId,
                        position = nextPosition++
                    )
                )
            }
            else {
                val exerciseId = viewModel.insertExercise(
                    Exercise(
                        exerciseId = 0,
                        exerciseTitle = title,
                        exerciseNote = ""
                    )
                )
                viewModel.insertWorkoutExerciseCrossRef(
                    WorkoutExerciseCrossRef(
                        workout_id = args.workoutId,
                        exercise_id = exerciseId.toInt(),
                        position = nextPosition++
                    )
                )
            }
        }

        binding.etAddExercise.text.clear()
        requireActivity().hideKeyboard()
    }

    private fun deleteExercise(workoutId: Int, exerciseId: Int) {
//        viewModel.deleteExercise(exerciseId)
        viewModel.deleteWorkoutExerciseCrossRef(
            workoutId = workoutId,
            exerciseId = exerciseId
        )
        nextPosition--
    }

    private fun updateExercise(
        exercise: Exercise,
        newTitle: String = exercise.exerciseTitle
    ) {
        viewModel.updateExercise(
            Exercise(
                exerciseId = exercise.exerciseId,
                exerciseTitle = newTitle,
                exerciseNote = exercise.exerciseNote
            )
        )
    }

    private fun updateWorkoutExerciseCrossRef(workoutId: Int, exerciseId: Int, newPosition: Int) {
        viewModel.updateWorkoutExerciseCrossRef(
            WorkoutExerciseCrossRef(
                workout_id = workoutId,
                exercise_id = exerciseId,
                position = newPosition
            )
        )
    }

    private fun exercisesPopUpMenu(view: View) {
        val popup = PopupMenu(binding.root.context, view)
        popup.menuInflater.inflate(R.menu.single_workout_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.option_edit -> {

                    binding.tvWorkoutTitle.visibility = View.GONE
                    binding.btnWorkoutOptions.visibility = View.GONE
                    binding.etWorkoutTitle.visibility = View.VISIBLE
                    binding.etWorkoutTitle.setText(workout.workoutTitle)
                    binding.btnEditWorkoutCancel.visibility = View.VISIBLE
                    binding.btnEditWorkoutSave.visibility = View.VISIBLE

                    // set focus and show soft keyboard
                    binding.etWorkoutTitle.requestFocus()
                    WindowCompat.getInsetsController(requireActivity().window, binding.etWorkoutTitle).show(
                        WindowInsetsCompat.Type.ime())

                    binding.btnEditWorkoutSave.setOnClickListener {
                        // check if empty
                        val title = binding.etWorkoutTitle.text.toString()
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
                            requireActivity().hideKeyboard()

                            // update database and hide editText and cancel+save button, show title and edit button
                            viewModel.updateWorkout(
                                Workout(
                                    workoutId = workout.workoutId,
                                    workoutTitle = title,
                                    workoutPosition = workout.workoutPosition
                                )
                            )
                            binding.tvWorkoutTitle.visibility = View.VISIBLE
                            binding.tvWorkoutTitle.text = title
                            binding.btnWorkoutOptions.visibility = View.VISIBLE
                            binding.etWorkoutTitle.visibility = View.GONE
                            binding.btnEditWorkoutCancel.visibility = View.GONE
                            binding.btnEditWorkoutSave.visibility = View.GONE

                            Toast.makeText(
                                requireContext(),
                                R.string.WorkoutUpdated,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    binding.btnEditWorkoutCancel.setOnClickListener {
                        requireActivity().hideKeyboard()
                        // hide and show relevant elements
                        binding.tvWorkoutTitle.visibility = View.VISIBLE
                        binding.btnWorkoutOptions.visibility = View.VISIBLE
                        binding.etWorkoutTitle.visibility = View.GONE
                        binding.btnEditWorkoutCancel.visibility = View.GONE
                        binding.btnEditWorkoutSave.visibility = View.GONE
                    }

                    true
                }
                else -> true
            }
        }
        popup.show()
    }

    private fun singleExercisePopupMenu(btnView: View, exercise: Exercise) {
        val popup = PopupMenu(binding.root.context, btnView)
        popup.menuInflater.inflate(R.menu.exercises_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.option_delete -> {
                    deleteExercise(workoutId = args.workoutId, exerciseId = exercise.exerciseId)
                    true
                }
                R.id.option_edit -> {
                    // hide exercise title and edit button, show edit Text and cancel+save button

                    // TODO refactor this and delete cancel button, make back button functionality of cancel, don't forget to set default behavior of back button afterwards (see setBackButton() in SingleExerciseActivity)

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
                            .findViewById<ImageButton>(R.id.btnSingleExerciseSave)
                    val editExerciseCancelButton: ImageButton =
                        binding.rvExercises.findContainingItemView(btnView)!!
                            .findViewById<ImageButton>(R.id.btnEditExerciseCancel)

                    // hide and show relevant elements
                    exerciseTextView.visibility = View.GONE
                    editExerciseButton.visibility = View.GONE
                    editExerciseEditText.visibility = View.VISIBLE
                    editExerciseEditText.setText(exercise.exerciseTitle)
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
                            updateExercise(exercise, title)
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

    private fun initGesture() {
        val gesture = object: Gesture() {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPosition = viewHolder.absoluteAdapterPosition
                val toPosition = target.absoluteAdapterPosition

                // swap position in db
                val crossRefFromPosition = viewModel.getWorkoutExerciseCrossRefByPosition(fromPosition)
                val crossRefToPosition = viewModel.getWorkoutExerciseCrossRefByPosition(toPosition)

                updateWorkoutExerciseCrossRef(
                    workoutId = crossRefFromPosition.workout_id,
                    exerciseId = crossRefFromPosition.exercise_id,
                    newPosition = toPosition
                )
                updateWorkoutExerciseCrossRef(
                    workoutId = crossRefToPosition.workout_id,
                    exerciseId = crossRefToPosition.exercise_id,
                    newPosition = fromPosition
                )

                adapter.notifyItemMoved(fromPosition, toPosition)
                return false
            }
        }
        val touchHelper = ItemTouchHelper(gesture)
        touchHelper.attachToRecyclerView(binding.rvExercises)
    }

    private fun initRecyclerView() {
        adapter = ExercisesAdapter()
        binding.rvExercises.adapter = adapter
        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())

        var listSize = 0
        viewModel.exercisesOfWorkout.observe(viewLifecycleOwner) {
            adapter.setList(it)

            // cannot use nextPosition in if statement as it gets incremented in saveWorkoutData()
            if (listSize != it.size) {
                nextPosition = it.size
                listSize = it.size
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}