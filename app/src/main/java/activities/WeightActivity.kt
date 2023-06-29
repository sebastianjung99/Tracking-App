package activities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.trackingapp.databinding.FragmentWeightBinding
import data.TrackingAppDatabase
import viewmodels.BodyMetricsViewModel
import viewmodels.BodyMetricsViewModelFactory


/**
 * A simple [Fragment] subclass.
 */
class WeightActivity : Fragment() {

    var _binding: FragmentWeightBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: BodyMetricsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWeightBinding.inflate(inflater, container, false)

        val bodyMetricsDao = TrackingAppDatabase.getInstance(requireActivity().application).bodyMetricsDao()
        val bodyMetricsFactory = BodyMetricsViewModelFactory(bodyMetricsDao)
        viewModel = ViewModelProvider(this, bodyMetricsFactory).get(BodyMetricsViewModel::class.java)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}