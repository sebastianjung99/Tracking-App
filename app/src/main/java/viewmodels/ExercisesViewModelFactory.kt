package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import data.ExercisesDao

class ExercisesViewModelFactory(
    private val dao: ExercisesDao,
    private val workoutId: Int
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ExercisesViewModel::class.java)) {
            return ExercisesViewModel(dao, workoutId) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}