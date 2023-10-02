package activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.trackingapp.databinding.FragmentSelectedDataBinding

class SelectedDataActivity: Fragment() {

    val args: SelectedDataActivityArgs by navArgs()

    var _binding: FragmentSelectedDataBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectedDataBinding.inflate(inflater, container, false)

        binding.tvPlaceHolderTest.text = args.selectedData

        return binding.root
    }
}