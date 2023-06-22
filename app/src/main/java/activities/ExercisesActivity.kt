package activities

import adapters.ExercisesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentExercisesBinding
import com.google.android.material.snackbar.Snackbar
import data.Exercises
import data.ExerciseSet
import utils.Utils.hideKeyboard

/**
 * A simple [Fragment] subclass.
 */
class ExercisesActivity : Fragment() {

    var _binding: FragmentExercisesBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)

        var setsList: MutableList<ExerciseSet> = mutableListOf(
            ExerciseSet(1, 12, 100),
            ExerciseSet(2, 10, 100),
            ExerciseSet(3, 11, 95)
        )
        var exerciseList = mutableListOf(
            Exercises("Sample Exercise", setsList),
            Exercises("Another Sample Exercise", setsList)
        )

        val adapter = ExercisesAdapter(exerciseList)
        binding.rvExercises.adapter = adapter
        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())

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
                exerciseList.add(
                    Exercises(
                        binding.etAddExercise.text.toString(),
                        mutableListOf(ExerciseSet(1, 0, 0))
                    )
                )
                binding.etAddExercise.text.clear()
                requireActivity().hideKeyboard()
            }
        }

        return binding.root
    }


}