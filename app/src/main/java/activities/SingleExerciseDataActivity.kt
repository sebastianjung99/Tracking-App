package activities

import adapters.ExercisesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.trackingapp.databinding.FragmentSingleExerciseDataBinding

class SingleExerciseDataActivity: Fragment() {

    val args: SingleExerciseDataActivityArgs by navArgs()

    var _binding: FragmentSingleExerciseDataBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSingleExerciseDataBinding.inflate(inflater, container, false)

        binding.tvPlaceHolderTest.text = "exercise id: " + args.exerciseId.toString()

        return binding.root
    }
}