package activities

import adapters.ExerciseHistoricSetAdapter
import adapters.ExerciseSetAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentSingleExerciseBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import data.Exercise
import data.ExerciseSet
import data.TrackingAppDatabase
import kotlinx.coroutines.launch
import utils.Utils.hideKeyboard
import viewmodels.WorkoutViewModel
import viewmodels.WorkoutViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 */
class SingleExerciseActivity: Fragment() {

    val args: SingleExerciseActivityArgs by navArgs()

    var _binding: FragmentSingleExerciseBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: WorkoutViewModel
    private lateinit var currentSetsAdapter: ExerciseSetAdapter
    private lateinit var historicSetsAdapter: ExerciseHistoricSetAdapter

    private lateinit var exercise: Exercise

    private var setNumber = 1
    private var exerciseSetsToday = mutableListOf<ExerciseSet>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSingleExerciseBinding.inflate(inflater, container, false)
        val dao = TrackingAppDatabase.getInstance(requireActivity().application).workoutDao()
        val factory = WorkoutViewModelFactory(dao, args.workoutId, args.exerciseId)
        viewModel = ViewModelProvider(this, factory).get(WorkoutViewModel::class.java)

        // set exercise title
        lifecycleScope.launch {
            exercise = viewModel.getExerciseById(args.exerciseId)
            binding.tvExerciseTitle.text = exercise.exerciseTitle
            // if title is empty, hide element and set margins
            if (exercise.exerciseNote.isEmpty()) {
                binding.cvExerciseNotes.visibility = View.GONE
                val param = binding.cvLatestReps.layoutParams as ViewGroup.MarginLayoutParams
                param.setMargins(0, 0, 0, 0)
                binding.cvLatestReps.layoutParams = param

            }
            else {
                binding.tvExerciseNotes.text = exercise.exerciseNote
            }
        }

        initRecyclerView()

        binding.btnSingleExerciseBack.setOnClickListener {
            findNavController().navigate(SingleExerciseActivityDirections.actionSingleExerciseToExercises(args.workoutId))
        }

        binding.btnSingleExerciseOptions.setOnClickListener {
            singleExercisePopupMenu(it)
        }

        binding.btnAddSet.setOnClickListener {
            saveExerciseSet()
        }

        currentSetsAdapter.setOnItemClickListener(object: ExerciseSetAdapter.onItemClickListener {
            override fun onItemClick(position: Int, view: View, exerciseSet: ExerciseSet) {
                // check if it's a dropset that got clicked on
                if (exerciseSet.setNumber != Int.MAX_VALUE) {
                    singleExerciseSetPopUpMenu(view, exerciseSet)
                }
                // if it's a dropset, the only option is to delete it
                else {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.ConfirmDeleteSetTitle))
                        .setNegativeButton(resources.getString(R.string.Cancel)) { dialog, which ->
                            // nothing
                        }
                        .setPositiveButton(resources.getString(R.string.Delete)) { dialog, which ->
                            deleteExerciseSet(exerciseSet)
                        }
                        .show()
                }
            }
        })

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

    fun saveExerciseSet(dropSetOfSetId: Long = 0) {

        // request focus because app will crash if textEdit element with OnFocusChangeListener
        // has focus while recyclerview gets altered
        binding.root.requestFocus()
        requireActivity().hideKeyboard()

        lifecycleScope.launch {
            val today = Calendar.getInstance()
            var insertSet = ExerciseSet(
                exerciseSetId = 0,
                exerciseId = args.exerciseId,
                dropSetOfSetId = dropSetOfSetId,
                setNumber = setNumber++,
                repetitions = 10,
                weight = 20,
                note = "",
                weightDay = today.get(Calendar.DAY_OF_MONTH),
                weightMonth = today.get(Calendar.MONTH) + 1,
                weightYear = today.get(Calendar.YEAR)
            )

            // dropSetOfSetId is not passed (= 0) if no dropset
            // check if dropset, if yes change set number to 0 (= dropset)
            if (dropSetOfSetId != 0L) {
                insertSet = insertSet.copy(setNumber = Int.MAX_VALUE)
                setNumber--
                viewModel.insertExerciseSet(insertSet)
            }
            // if not, change set number, then insert, get id back and change dropSetOfSetId to id
            else {
                val id = viewModel.insertExerciseSet(insertSet)
                insertSet = insertSet.copy(exerciseSetId = id.toInt(), dropSetOfSetId = id)
                updateExerciseSet(insertSet)
            }

            exerciseSetsToday.add(insertSet)
        }
    }

    private fun deleteExerciseSet(exerciseSetToDelete: ExerciseSet) {

        // request focus because app will crash if textEdit element with OnFocusChangeListener
        // has focus while recyclerview gets altered
        binding.root.requestFocus()
        requireActivity().hideKeyboard()

        viewModel.deleteExerciseSet(exerciseSetToDelete.exerciseSetId)
        getTodaysSets()
        setNumber--

        // update set numbers for sets after deleted one
        for (i in 0 until exerciseSetsToday.size - 1) {
            val currentSet = exerciseSetsToday[i]
            if ((currentSet.setNumber > exerciseSetToDelete.setNumber) and (currentSet.setNumber != Int.MAX_VALUE)) {
                val newSet = currentSet.copy(setNumber = exerciseSetsToday[i].setNumber - 1)
                updateExerciseSet(newSet)
            }
        }
    }

    private fun updateExerciseSet(exerciseSet: ExerciseSet) {

        // request focus because app will crash if textEdit element with OnFocusChangeListener
        // has focus while recyclerview gets altered
        binding.root.requestFocus()
        requireActivity().hideKeyboard()

        viewModel.updateExerciseSet(exerciseSet)
        // getTodaysSets()
    }

    private fun singleExercisePopupMenu(view: View) {
        val popup = PopupMenu(binding.root.context, view)
        popup.menuInflater.inflate(R.menu.single_exercise_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.option_edit -> {

                    binding.tvExerciseTitle.visibility = View.GONE
                    binding.btnSingleExerciseOptions.visibility = View.GONE
                    binding.etExerciseTitle.visibility = View.VISIBLE
                    binding.etExerciseTitle.setText(exercise.exerciseTitle)
                    binding.btnEditExerciseCancel.visibility = View.VISIBLE
                    binding.btnEditExerciseSave.visibility = View.VISIBLE

                    // set focus and show soft keyboard
                    binding.etExerciseTitle.requestFocus()
                    WindowCompat.getInsetsController(requireActivity().window, binding.etExerciseTitle).show(
                        WindowInsetsCompat.Type.ime())

                    binding.btnEditExerciseSave.setOnClickListener {
                        // check if empty
                        val title = binding.etExerciseTitle.text.toString()
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
                            requireActivity().hideKeyboard()

                            // update database and hide editText and cancel+save button, show title and edit button
                            viewModel.updateExercise(
                                Exercise(
                                    exerciseId = exercise.exerciseId,
                                    exerciseTitle = title,
                                    exerciseNote = exercise.exerciseNote
                                )
                            )
                            binding.tvExerciseTitle.visibility = View.VISIBLE
                            binding.tvExerciseTitle.text = title
                            binding.btnSingleExerciseOptions.visibility = View.VISIBLE
                            binding.etExerciseTitle.visibility = View.GONE
                            binding.btnEditExerciseCancel.visibility = View.GONE
                            binding.btnEditExerciseSave.visibility = View.GONE

                            Toast.makeText(
                                requireContext(),
                                R.string.ExerciseUpdated,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    binding.btnEditExerciseCancel.setOnClickListener {
                        requireActivity().hideKeyboard()
                        // hide and show relevant elements
                        binding.tvExerciseTitle.visibility = View.VISIBLE
                        binding.btnSingleExerciseOptions.visibility = View.VISIBLE
                        binding.etExerciseTitle.visibility = View.GONE
                        binding.btnEditExerciseCancel.visibility = View.GONE
                        binding.btnEditExerciseSave.visibility = View.GONE
                    }

                    true
                }
                R.id.option_exerciseAddNote -> {
                    // TODO implement option_exerciseAddNote functionality
                    true
                }
                R.id.option_showData -> {
                    // TODO implement option_showData functionality
                    true
                }
                else -> true
            }
        }
        popup.show()
    }

    private fun singleExerciseSetPopUpMenu(btnView: View, exerciseSet: ExerciseSet) {
        val popup = PopupMenu(binding.root.context, btnView)
        popup.menuInflater.inflate(R.menu.exerciseset_menu, popup.menu)
        popup.setOnMenuItemClickListener {

            when (it.itemId) {
                R.id.option_addDropset -> {
                    saveExerciseSet(exerciseSet.exerciseSetId.toLong())
                    true
                }
                R.id.option_addNote -> {
                    // TODO: implement addNote functionality
                    true
                }
                R.id.option_delete -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.ConfirmDeleteSetTitle))
                        .setNegativeButton(resources.getString(R.string.Cancel)) { dialog, which ->
                            // nothing
                        }
                        .setPositiveButton(resources.getString(R.string.Delete)) { dialog, which ->
                            deleteExerciseSet(exerciseSet)
                        }
                        .show()
                    true
                }
                else -> true
            }
        }
        popup.show()
    }

    private fun getTodaysSets() {
        setNumber = 1
        exerciseSetsToday.clear()
        // test
        val historicSetsAll =
            viewModel.getExerciseSetsByExerciseWorkout(args.exerciseId, args.workoutId)
        val today = LocalDate.now()

        for (i in historicSetsAll.indices) {
            val setI = historicSetsAll[i]
            val date = LocalDate.of(setI.weightYear, setI.weightMonth, setI.weightDay)

            // do not count dropsets (setNumber is 0 if dropset)
            if (date.isEqual(today) and (setI.setNumber != 0)){
                setNumber++
                exerciseSetsToday.add(setI)
            }
            else if (!date.isEqual(today))
                break
        }
    }

    private fun initRecyclerView() {
        currentSetsAdapter = ExerciseSetAdapter()
        binding.rvExerciseSets.adapter = currentSetsAdapter
        binding.rvExerciseSets.layoutManager = LinearLayoutManager(requireContext())

        historicSetsAdapter = ExerciseHistoricSetAdapter()
        binding.rvExerciseSetsHistoric.adapter = historicSetsAdapter
        binding.rvExerciseSetsHistoric.layoutManager = LinearLayoutManager(requireContext())

        var listSize = 0
        viewModel.setsOfExerciseOfWorkoutToday.observe(viewLifecycleOwner) {
            currentSetsAdapter.setList(it)
            currentSetsAdapter.notifyDataSetChanged()

            // auto scroll down if item got added
            if (listSize < it.size) {
                binding.rvExerciseSets.scrollToPosition(currentSetsAdapter.itemCount - 1)
            }

            listSize = it.size
        }


        // get last sets
        var historicSets: List<ExerciseSet>? = listOf()
        lifecycleScope.launch {
            val historicSetsAll = viewModel.getExerciseSetsByExerciseWorkout(args.exerciseId, args.workoutId)
            val today = LocalDate.now()

            for (i in historicSetsAll.indices) {
                val setI = historicSetsAll[i]
                val date = LocalDate.of(setI.weightYear, setI.weightMonth, setI.weightDay)

                // need to count sets on initial load so we know what setNumber the next inserted Set has
                if (date.isEqual(today)) {
                    setNumber++
                    exerciseSetsToday.add(setI)
                }

                // find first set that has been saved before today
                if (date.isBefore(today)) {
                    historicSets = viewModel.getExerciseSetsByExerciseWorkoutDateAsList(
                        args.exerciseId,
                        args.workoutId,
                        setI.weightDay,
                        setI.weightMonth,
                        setI.weightYear
                    )
                    binding.tvHistoricSetDate.text = LocalDate.of(
                        setI.weightYear,
                        setI.weightMonth,
                        setI.weightDay
                    ).format(DateTimeFormatter.ofPattern("d. MMMM")).toString()

                    break
                }
            }

            // reverse list since it's ordered by exerciseSet_id, but higher exerciseSet_id means
            // higher exerciseSet_setNumber, and we want the list displayed by ascending setNumber,
            // not descending
            historicSets?.let { historicSetsAdapter.setList(it) }
            historicSetsAdapter.notifyDataSetChanged()
        }

    }

}