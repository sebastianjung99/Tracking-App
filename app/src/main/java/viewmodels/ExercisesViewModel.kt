package viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Exercises
import data.ExercisesDao
import kotlinx.coroutines.launch

class ExercisesViewModel(
    private val dao: ExercisesDao,
    private val workoutId: Int
): ViewModel() {
    val exercises = dao.getAllExercises()

    fun insertExercise(exercises: Exercises) = viewModelScope.launch {
        dao.insertExercise(exercises)
    }

    fun deleteExercise(exercise_id: Int) = viewModelScope.launch {
        dao.deleteExercise(exercise_id)
    }

    fun updateExercise(exercises: Exercises) = viewModelScope.launch {
        dao.updateExercise(exercises)
    }

    fun getExerciseByTitle(exerciseTitle: String): Exercises? {
        return dao.getExerciseByTitle(exerciseTitle)
    }

}