package activities

import adapters.WeightTrackingRecordAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentWeightBinding
import com.google.android.material.snackbar.Snackbar
import data.TrackingAppDatabase
import data.WeightTrackingRecord
import utils.Utils.hideKeyboard
import viewmodels.BodyMetricsViewModel
import viewmodels.BodyMetricsViewModelFactory
import java.util.Calendar


/**
 * A simple [Fragment] subclass.
 */
class WeightActivity : Fragment() {

    var _binding: FragmentWeightBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: BodyMetricsViewModel
    private lateinit var adapter: WeightTrackingRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWeightBinding.inflate(inflater, container, false)
        val bodyMetricsDao = TrackingAppDatabase.getInstance(requireActivity().application).bodyMetricsDao()
        val bodyMetricsFactory = BodyMetricsViewModelFactory(bodyMetricsDao)
        viewModel = ViewModelProvider(this, bodyMetricsFactory).get(BodyMetricsViewModel::class.java)

        initRecyclerView()

        binding.btnAddWeightMeasurement.setOnClickListener {
            val weight: String = binding.etWeight.text.toString()
            val bodyFat: String = binding.etBodyFat.text.toString()
            if (weight.trim().isEmpty() or bodyFat.trim().isEmpty()) {
                // TODO: replace action with vector graphic?
                Snackbar.make(
                    requireContext(),
                    binding.root,
                    getString(R.string.AddExerciseEmptyEditTextPrompt),
                    Snackbar.LENGTH_SHORT
                ).setAction("X"){}.show()
            }
            else {
                saveWeightTrackingRecord(weight, bodyFat)
            }
        }

        return binding.root
    }

    private fun saveWeightTrackingRecord(
        weight: String,
        bodyFat: String
    ) {
        viewModel.insertWeightTrackingRecord(
            WeightTrackingRecord(
                weightId = 0,
                weightWeight = weight.toDouble(),
                weightBodyFat = bodyFat.toDouble(),
                weightDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                weightMonth = Calendar.getInstance().get(Calendar.MONTH) + 1,
                weightYear = Calendar.getInstance().get(Calendar.YEAR)
            )
        )
        binding.etWeight.text.clear()
        binding.etBodyFat.text.clear()
        requireActivity().hideKeyboard()
    }

    private fun deleteWeightTrackingRecord(weightId: Int) {
        viewModel.deleteWeightTrackingRecord(weightId)
    }

    private fun updateWeightTrackingRecord(weightTrackingRecord: WeightTrackingRecord) {
        viewModel.updateWeightTrackingRecord(weightTrackingRecord)
    }

    private fun initRecyclerView() {
        adapter = WeightTrackingRecordAdapter()
        binding.rvWeightTrackingRecords.adapter = adapter
        binding.rvWeightTrackingRecords.layoutManager = LinearLayoutManager(requireContext())

        // display workouts list
        viewModel.weightTrackingRecords.observe(viewLifecycleOwner) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}