package viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Exercises
import data.Workouts
import data.WorkoutsDao
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val dao: WorkoutsDao,
    private val workoutId: Int
): ViewModel() {
    val workouts = dao.getAllWorkouts()
//    val exercisesByWorkoutId = dao.getExercisesByWorkoutId(workoutId)

    fun insertWorkout(workouts: Workouts) = viewModelScope.launch {
        dao.insertWorkout(workouts)
    }

    fun deleteWorkout(workouts_id: Int) = viewModelScope.launch {
        dao.deleteWorkout(workouts_id)
    }

    fun updateWorkout(workouts: Workouts) = viewModelScope.launch {
        dao.updateWorkout(workouts)
    }

    fun getWorkout(workoutId: Int): Workouts {
        return dao.getWorkout(workoutId)
    }
}