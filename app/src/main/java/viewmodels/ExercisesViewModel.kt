package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Exercise
import data.ExercisesDao
import kotlinx.coroutines.launch

class ExercisesViewModel(
    private val dao: ExercisesDao,
    private val workoutId: Int
): ViewModel() {
    val exercises = dao.getAllExercises()

    suspend fun insertExercise(exercise: Exercise) = dao.insertExercise(exercise)

    fun deleteExercise(exercise_id: Int) = viewModelScope.launch {
        dao.deleteExercise(exercise_id)
    }

    fun updateExercise(exercise: Exercise) = viewModelScope.launch {
        dao.updateExercise(exercise)
    }

}