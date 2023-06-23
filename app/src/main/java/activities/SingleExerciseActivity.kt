package activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.trackingapp.databinding.FragmentSingleExerciseBinding

/**
 * A simple [Fragment] subclass.
 */
class SingleExerciseActivity: Fragment() {
    var _binding: FragmentSingleExerciseBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSingleExerciseBinding.inflate(inflater, container, false)

        // TODO: implement activity functionalities

        return binding.root
    }
}