package viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.Exercise
import data.ExerciseSet
import data.Workout
import data.WorkoutDao
import data.relations.ExerciseWithWorkouts
import data.relations.WorkoutExerciseCrossRef
import data.relations.WorkoutWithExercises
import kotlinx.coroutines.launch
import java.util.Calendar

class WorkoutViewModel(
    private val dao: WorkoutDao,
    private val workoutId: Int,
    private val exerciseId: Int
): ViewModel() {
    val workouts = dao.getAllWorkoutsOrderedByWorkoutPositionASC()
    val exercisesOfWorkout = dao.getExercisesOfWorkoutOrderedByPosition(workoutId)

    val setsOfExerciseOfWorkoutToday = dao.getExerciseSetsByExerciseWorkoutDate(
        workoutId = workoutId,
        exerciseId = exerciseId,
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
        month = Calendar.getInstance().get(Calendar.MONTH) + 1,
        year = Calendar.getInstance().get(Calendar.YEAR)
    )



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

    fun getWorkoutById(workout_id: Int) = dao.getWorkoutById(workout_id)

    fun getWorkoutByPosition(workoutPosition: Int) = dao.getWorkoutByPosition(workoutPosition)



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

    fun getExerciseById(exerciseId: Int): Exercise {
        return dao.getExerciseById(exerciseId)
    }



    /*********************************************/
    /**********       EXERCISE SET      **********/
    /*********************************************/
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet) = dao.insertExerciseSet(exerciseSet)

    fun deleteExerciseSet(exerciseSet_id: Int) = viewModelScope.launch {
        dao.deleteExerciseSet(exerciseSet_id)
    }

    fun updateExerciseSet(exerciseSet: ExerciseSet) = viewModelScope.launch {
        dao.updateExerciseSet(exerciseSet)
    }

    fun getAllExerciseSets() = dao.getAllExerciseSets()

    fun getExerciseSetsByExerciseWorkoutDateAsList(
        exerciseId: Int,
        workoutId: Int,
        day: Int,
        month: Int,
        year: Int
    ) = dao.getExerciseSetsByExerciseWorkoutDateAsList(workoutId, exerciseId, day, month, year)

    fun getExerciseSetsByExerciseWorkout(
        exerciseId: Int,
        workoutId: Int
    ): List<ExerciseSet> = dao.getExerciseSetsByExerciseWorkout(exerciseId, workoutId)





    /*********************************************/
    /**********         CROSSREF        **********/
    /*********************************************/
    fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef) = viewModelScope.launch {
        dao.insertWorkoutExerciseCrossRef(crossRef)
    }

    fun deleteWorkoutExerciseCrossRef(workoutId: Int, exerciseId: Int) = viewModelScope.launch {
        dao.deleteWorkoutExerciseCrossRef(workoutId, exerciseId)
    }

    fun updateWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef) = viewModelScope.launch {
        dao.updateWorkoutExerciseCrossRef(crossRef)
    }

    fun getWorkoutsOfExercise(exerciseId: Int): LiveData<ExerciseWithWorkouts> {
        return dao.getWorkoutsOfExercise(exerciseId)
    }

    fun getExercisesOfWorkout(workoutId: Int): LiveData<WorkoutWithExercises> {
        return dao.getExercisesOfWorkout(workoutId)
    }

    fun getWorkoutExerciseCrossRefByPosition(position: Int): WorkoutExerciseCrossRef {
        return dao.getWorkoutExerciseCrossRefByPosition(position)
    }

    fun getExercisesOfWorkoutOrderedByPosition(workoutId: Int): LiveData<List<Exercise>> {
        return dao.getExercisesOfWorkoutOrderedByPosition(workoutId)
    }
}