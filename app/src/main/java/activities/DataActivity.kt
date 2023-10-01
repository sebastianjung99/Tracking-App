package activities

import adapters.DataAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentDataBinding

class DataActivity: Fragment() {
    var _binding: FragmentDataBinding? = null
    val binding get() = _binding!!

    private lateinit var adapter: DataAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root
    }

    private  fun initRecyclerView() {
        adapter = DataAdapter()
        binding.rvData.adapter = adapter
        binding.rvData.layoutManager = LinearLayoutManager(requireContext())

        // add divider
        var dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        ResourcesCompat.getDrawable(resources, R.drawable.divider, null)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        binding.rvData.addItemDecoration(dividerItemDecoration)

        // display data selection list
        var dataList = arrayListOf<String>(
            "Exercises",
            "Weight"
        )
        adapter.setList(dataList)
    }
}