package viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Exercise
import data.Workout
import data.WorkoutDao
import data.relations.ExerciseWithWorkouts
import data.relations.WorkoutExerciseCrossRef
import data.relations.WorkoutWithExercises
import kotlinx.coroutines.launch

class WorkoutViewModel(
    private val dao: WorkoutDao,
    private val workoutId: Int
): ViewModel() {
    val workouts = dao.getAllWorkouts()
    var exercisesOfWorkout = dao.getExercisesOfWorkout(workoutId)



    /*********************************************/
    /**********         WORKOUT         **********/
    /*********************************************/
    fun insertWorkout(workout: Workout) = viewModelScope.launch {
        dao.insertWorkout(workout)
    }

    fun deleteWorkout(workouts_id: Int) = viewModelScope.launch {
        dao.deleteWorkout(workouts_id)
    }

    fun updateWorkout(workout: Workout) = viewModelScope.launch {
        dao.updateWorkout(workout)
    }



    /*********************************************/
    /**********         EXERCISE        **********/
    /*********************************************/
    suspend fun insertExercise(exercise: Exercise) = dao.insertExercise(exercise)

    fun deleteExercise(exercise_id: Int) = viewModelScope.launch {
        dao.deleteExercise(exercise_id)
    }

    fun updateExercise(exercise: Exercise) = viewModelScope.launch {
        dao.updateExercise(exercise)
    }

    fun getExerciseByTitle(exerciseTitle: String): Exercise {
        return dao.getExerciseByTitle(exerciseTitle)
    }





    /*********************************************/
    /**********         CROSSREF        **********/
    /*********************************************/
    fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef) = viewModelScope.launch {
        dao.insertWorkoutExerciseCrossRef(crossRef)
    }

    fun deleteWorkoutExerciseCrossRef(workoutId: Int, exerciseId: Int) = viewModelScope.launch {
        dao.deleteWorkoutExerciseCrossRef(workoutId, exerciseId)
    }

    fun getWorkoutsOfExercise(exerciseId: Int): LiveData<ExerciseWithWorkouts> {
        return dao.getWorkoutsOfExercise(exerciseId)
    }

    fun getExercisesOfWorkout(workoutId: Int): LiveData<WorkoutWithExercises> {
        return dao.getExercisesOfWorkout(workoutId)
    }
}