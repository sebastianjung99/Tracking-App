package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Workouts
import data.WorkoutsDao
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val dao: WorkoutsDao
): ViewModel() {
    val workouts = dao.getAllWorkouts()

    fun insertWorkout(workouts: Workouts) = viewModelScope.launch {
        dao.insertWorkout(workouts)
    }

    fun deleteWorkout(workouts_id: Int) = viewModelScope.launch {
        dao.deleteWorkout(workouts_id)
    }

    fun updateWorkout(workouts: Workouts) = viewModelScope.launch {
        dao.updateWorkout(workouts)
    }

}