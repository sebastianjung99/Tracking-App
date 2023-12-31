package activities

import adapters.ExercisesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.R
import com.example.trackingapp.databinding.FragmentExercisesDataBinding
import data.Exercise
import data.TrackingAppDatabase
import viewmodels.WorkoutViewModel
import viewmodels.WorkoutViewModelFactory

class ExercisesDataActivity: Fragment() {

    val args: ExercisesDataActivityArgs by navArgs()

    var _binding: FragmentExercisesDataBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: WorkoutViewModel
    private lateinit var adapter: ExercisesAdapter

    // set to False to sort descending
    private var sortByAsc = true

    private var sortPopupCheckedItemIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesDataBinding.inflate(inflater, container, false)

        val workoutDao = TrackingAppDatabase.getInstance(requireActivity().application).workoutDao()
        val workoutFactory = WorkoutViewModelFactory(workoutDao, 0, 0)
        viewModel = ViewModelProvider(this, workoutFactory).get(WorkoutViewModel::class.java)

        binding.tvSelectedDataTitle.text = args.selectedData

        initRecyclerView()

        initItemClick()

        setSortButton()

        setAscDescButton()

        return binding.root
    }

    private fun initRecyclerView() {
        adapter = ExercisesAdapter()
        binding.rvSelectedData.adapter = adapter
        binding.rvSelectedData.layoutManager = LinearLayoutManager(requireContext())

        // add divider
        var dividerItemDecoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        ResourcesCompat.getDrawable(resources, R.drawable.divider, null)?.let {
            dividerItemDecoration.setDrawable(it)
        }
        binding.rvSelectedData.addItemDecoration(dividerItemDecoration)

        viewModel.allExercises.observe(viewLifecycleOwner) {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initItemClick() {
        adapter.setOnItemClickListener(object: ExercisesAdapter.onItemClickListener {
            override fun onItemClick(position: Int, view: View, exercise: Exercise) {
                when (view.id) {
                    binding.root.id -> {
                        val action = ExercisesDataActivityDirections.actionExercisesDataFragmentToSingleExerciseData(
                            exercise.exerciseId
                        )
                        findNavController().navigate(action)
                    }
                    R.id.btnEditExercise -> {
                        // TODO: copy edit exercise button functionality from ExercisesActivity
                    }
                }
            }
        })
    }

    private fun setSortButton() {
        binding.btnSortData.setOnClickListener {
            val popup = PopupMenu(binding.root.context, it)
            popup.menuInflater.inflate(R.menu.sort_menu, popup.menu)

            // set all items in menu to non-checkable
            for (i in 0 until popup.menu.size()) {
                popup.menu.getItem(i).setCheckable(false)
            }
            // set the one last selected to checkable and checked
            popup.menu.getItem(sortPopupCheckedItemIndex).setCheckable(true)
            popup.menu.getItem(sortPopupCheckedItemIndex).setChecked(true)

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.option_sortById -> {
                        adapter.sortById(sortByAsc)
                        adapter.notifyDataSetChanged()
                        sortPopupCheckedItemIndex = 0
                        true
                    }
                    R.id.option_sortByTitle -> {
                        adapter.sortByTitle(sortByAsc)
                        adapter.notifyDataSetChanged()
                        sortPopupCheckedItemIndex = 1
                        true
                    }
                    else -> true
                }
            }
            popup.show()
        }
    }

    private fun setAscDescButton() {
        binding.btnAscDesc.setOnClickListener {
            sortByAsc = !sortByAsc

            if (sortByAsc) {
                binding.btnAscDesc.rotation = 0F
            }
            else {
                binding.btnAscDesc.rotation = 180F
            }

            adapter.reverseList()
            adapter.notifyDataSetChanged()
        }
    }
}