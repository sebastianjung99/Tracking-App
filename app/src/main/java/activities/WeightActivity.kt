package activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentWeightBinding


/**
 * A simple [Fragment] subclass.
 */
class WeightActivity : Fragment() {

    var _binding: FragmentWeightBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWeightBinding.inflate(inflater, container, false)
        binding.textView2.setOnClickListener {
            findNavController().navigate(R.id.action_weight_to_workouts)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}