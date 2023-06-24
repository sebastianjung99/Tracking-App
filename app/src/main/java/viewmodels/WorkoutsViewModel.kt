package viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Workout
import data.WorkoutsDao
import data.relations.ExerciseWithWorkouts
import data.relations.WorkoutExerciseCrossRef
import data.relations.WorkoutWithExercises
import kotlinx.coroutines.launch

class WorkoutsViewModel(
    private val dao: WorkoutsDao,
    private val workoutId: Int
): ViewModel() {
    val workouts = dao.getAllWorkouts()
    var exercisesOfWorkout = dao.getExercisesOfWorkout(workoutId)

    fun insertWorkout(workout: Workout) = viewModelScope.launch {
        dao.insertWorkout(workout)
    }

    fun deleteWorkout(workouts_id: Int) = viewModelScope.launch {
        dao.deleteWorkout(workouts_id)
    }

    fun updateWorkout(workout: Workout) = viewModelScope.launch {
        dao.updateWorkout(workout)
    }



    fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef) = viewModelScope.launch {
        dao.insertWorkoutExerciseCrossRef(crossRef)
    }

    fun getWorkoutsOfExercise(exerciseId: Int): LiveData<List<ExerciseWithWorkouts>> {
        return dao.getWorkoutsOfExercise(exerciseId)
    }

    fun getExercisesOfWorkout(workoutId: Int): LiveData<WorkoutWithExercises> {
        return dao.getExercisesOfWorkout(workoutId)
    }

    fun reloadExercisesOfWorkout() = viewModelScope.launch {
        exercisesOfWorkout = dao.getExercisesOfWorkout(workoutId)
    }


}