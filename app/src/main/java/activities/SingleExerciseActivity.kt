package activities

import adapters.ExerciseHistoricSetAdapter
import adapters.ExerciseSetAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentSingleExerciseBinding
import data.ExerciseSet
import data.TrackingAppDatabase
import kotlinx.coroutines.launch
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

    private var setNumber = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSingleExerciseBinding.inflate(inflater, container, false)
        val dao = TrackingAppDatabase.getInstance(requireActivity().application).workoutDao()
        val factory = WorkoutViewModelFactory(dao, args.workoutId, args.exerciseId)
        viewModel = ViewModelProvider(this, factory).get(WorkoutViewModel::class.java)

        // set workout title
        lifecycleScope.launch {
            binding.tvExerciseTitle.text = viewModel.getExerciseTitleById(args.exerciseId)
        }

        initRecyclerView()

        binding.btnSingleExerciseBack.setOnClickListener {
            findNavController().navigate(SingleExerciseActivityDirections.actionSingleExerciseToExercises(args.workoutId))
        }

        binding.btnAddSet.setOnClickListener {
            saveExerciseSet()
        }

        // TODO: implement onClickListener for the button on each recyclerView item (to show options like delete and add note)
        currentSetsAdapter.setOnItemClickListener(object: ExerciseSetAdapter.onItemClickListener {
            override fun onItemClick(position: Int, view: View, exerciseSet: ExerciseSet) {
                singleExerciseSetPopUpMenu(view, exerciseSet)
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

        // TODO: remove save sets button

        return binding.root
    }

    // TODO: save, delete and update ExerciseSet database entry
    private fun saveExerciseSet() {
        lifecycleScope.launch {
            val today = Calendar.getInstance()
            viewModel.insertExerciseSet(
                ExerciseSet(
                    exerciseSetId = 0,
                    exerciseId = args.exerciseId,
                    setNumber = setNumber++,
                    repetitions = 10,
                    weight = 20,
                    weightDay = today.get(Calendar.DAY_OF_MONTH),
                    weightMonth = today.get(Calendar.MONTH) + 1,
                    weightYear = today.get(Calendar.YEAR)
                )
            )
        }
    }

    private fun singleExerciseSetPopUpMenu(btnView: View, exerciseSet: ExerciseSet) {
        val popup = PopupMenu(binding.root.context, btnView)
        popup.menuInflater.inflate(R.menu.exerciseset_menu, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.option_addDropset -> {
                    // TODO: implement addDropset functionality
                    true
                }
                R.id.option_addNote -> {
                    // TODO: implement addNote functionality
                    true
                }
                R.id.option_delete -> {
                    // TODO: implement delete functionality
                    true
                }
                else -> true
            }
        }
        popup.show()
    }

    private fun initRecyclerView() {
        currentSetsAdapter = ExerciseSetAdapter()
        binding.rvExerciseSets.adapter = currentSetsAdapter
        binding.rvExerciseSets.layoutManager = LinearLayoutManager(requireContext())

        historicSetsAdapter = ExerciseHistoricSetAdapter()
        binding.rvExerciseSetsHistoric.adapter = historicSetsAdapter
        binding.rvExerciseSetsHistoric.layoutManager = LinearLayoutManager(requireContext())

        viewModel.setsOfExerciseOfWorkoutToday.observe(viewLifecycleOwner) {
            currentSetsAdapter.setList(it)
            currentSetsAdapter.notifyDataSetChanged()
            // auto scroll to end of list on change
            binding.rvExerciseSets.scrollToPosition(currentSetsAdapter.itemCount - 1)
        }


        // get last sets
        var historicSets = mutableListOf<ExerciseSet>()
        lifecycleScope.launch {
            val historicSetsAll = viewModel.getExerciseSetsByExerciseWorkout(args.exerciseId, args.workoutId)
            val today = LocalDate.now()

            for (i in historicSetsAll.indices) {
                val setI = historicSetsAll[i]
                var done = false
                val date = LocalDate.of(setI.weightYear, setI.weightMonth, setI.weightDay)

                if (date.isEqual(today)) {
                    setNumber++
                }
                // find first set that has been saved before today
                else if (date.isBefore(today)) {
                    historicSets.add(setI)
                    binding.tvHistoricSetDate.text = LocalDate.of(
                        setI.weightYear,
                        setI.weightMonth,
                        setI.weightDay
                    ).format(DateTimeFormatter.ofPattern("d. MMMM")).toString()

                    // since sets are sorted descending by id, higher id means more recent date
                    // iterate from here and add all sets with same date to historicSets
                    for (j in i + 1 until historicSetsAll.size) {
                        val setJ = historicSetsAll[j]
                        if (
                            setJ.weightDay == setI.weightDay
                            && setJ.weightMonth == setI.weightMonth
                            && setJ.weightYear == setI.weightYear
                        ) {
                            historicSets.add(setJ)
                        // stop once date changes
                        } else {
                            done = true
                            break
                        }
                    }
                }
                if (done) break
            }

            // reverse list since it's ordered by exerciseSet_id, but higher exerciseSet_id means
            // higher exerciseSet_setNumber, and we want the list displayed by ascending setNumber,
            // not descending
            historicSetsAdapter.setList(historicSets.toList().asReversed())
            historicSetsAdapter.notifyDataSetChanged()
        }

    }

}