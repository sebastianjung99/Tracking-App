package activities

import adapters.ExercisesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.databinding.FragmentExercisesBinding
import data.Exercises
import data.ExerciseSet

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

//        binding.btnAddExercise.setOnClickListener {
//
//        }

        return binding.root
    }

}