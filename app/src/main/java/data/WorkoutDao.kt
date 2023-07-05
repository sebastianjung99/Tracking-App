package data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import data.relations.ExerciseWithWorkouts
import data.relations.WorkoutExerciseCrossRef
import data.relations.WorkoutWithExercises

@Dao
interface WorkoutDao {
    /*********************************************/
    /**********         WORKOUT         **********/
    /*********************************************/
    @Insert
    suspend fun insertWorkout(workout: Workout)

    @Update
    suspend fun updateWorkout(workout: Workout)

    @Query("DELETE FROM workouts_data_table WHERE workout_id = :workouts_id")
    suspend fun deleteWorkout(workouts_id: Int)

    @Query("SELECT * FROM workouts_data_table")
    fun getAllWorkouts(): LiveData<List<Workout>>



    /*********************************************/
    /**********         EXERCISE        **********/
    /*********************************************/
    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Query("DELETE FROM exercises_data_table WHERE exercise_id = :exerciseId")
    suspend fun deleteExercise(exerciseId: Int)

    @Query("SELECT * FROM exercises_data_table")
    fun getAllExercises(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises_data_table WHERE exercise_title = :exerciseTitle")
    fun getExerciseByTitle(exerciseTitle: String): Exercise



    /*********************************************/
    /**********       EXERCISE SET      **********/
    /*********************************************/
    @Insert
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet)

    @Update
    suspend fun updateExerciseSet(exerciseSet: ExerciseSet)

    @Query("DELETE FROM exerciseSet_data_table WHERE exerciseSet_id = :exerciseSet_id")
    suspend fun deleteExerciseSet(exerciseSet_id: Int)

    @Query("SELECT * FROM exerciseSet_data_table")
    fun getAllExerciseSets(): LiveData<List<ExerciseSet>>



    /*********************************************/
    /**********         CROSSREF        **********/
    /*********************************************/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef)

    @Query("DELETE FROM WorkoutExerciseCrossRef WHERE workout_id = :workoutId AND exercise_id = :exerciseId")
    suspend fun deleteWorkoutExerciseCrossRef(workoutId: Int, exerciseId: Int)

    @Query("SELECT * FROM exercises_data_table WHERE exercise_id = :exerciseId")
    fun getWorkoutsOfExercise(exerciseId: Int): LiveData<ExerciseWithWorkouts>

    @Query("SELECT * FROM workouts_data_table WHERE workout_id = :workoutId")
    fun getExercisesOfWorkout(workoutId: Int): LiveData<WorkoutWithExercises>
}