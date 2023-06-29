package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import data.BodyMetricsDao

class BodyMetricsViewModelFactory(
    private val dao: BodyMetricsDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BodyMetricsViewModel::class.java)) {
            return BodyMetricsViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}