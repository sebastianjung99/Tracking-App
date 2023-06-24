package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import data.WorkoutsDao

class WorkoutsViewModelFactory(
    private val dao: WorkoutsDao,
    private val workoutId: Int
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WorkoutsViewModel::class.java)) {
            return WorkoutsViewModel(dao, workoutId) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}