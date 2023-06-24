package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import data.WorkoutDao

class WorkoutViewModelFactory(
    private val dao: WorkoutDao,
    private val workoutId: Int
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            return WorkoutViewModel(dao, workoutId) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}