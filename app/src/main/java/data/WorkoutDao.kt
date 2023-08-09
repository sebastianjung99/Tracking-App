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

    @Query("DELETE FROM workouts_data_table WHERE workout_id = :workoutId")
    suspend fun deleteWorkout(workoutId: Int)

    @Query("SELECT * FROM workouts_data_table")
    fun getAllWorkouts(): LiveData<List<Workout>>

    @Query("SELECT * FROM workouts_data_table ORDER BY workout_position ASC")
    fun getAllWorkoutsOrderedByWorkoutPositionASC(): LiveData<List<Workout>>

    @Query("SELECT * FROM workouts_data_table WHERE workout_id = :workoutId")
    fun getWorkoutById(workoutId: Int): Workout

    @Query("SELECT * FROM workouts_data_table WHERE workout_position = :workoutPosition")
    fun getWorkoutByPosition(workoutPosition: Int): Workout




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

    @Query("SELECT * FROM exercises_data_table WHERE exercise_id = :exerciseId")
    fun getExerciseById(exerciseId: Int): Exercise



    /*********************************************/
    /**********       EXERCISE SET      **********/
    /*********************************************/
    /* TODO: add position array to ExerciseSet.kt so we can have different sets for the same
    exercise if exercise gets added to a workout multiple times */
    @Insert
    suspend fun insertExerciseSet(exerciseSet: ExerciseSet)

    @Update
    suspend fun updateExerciseSet(exerciseSet: ExerciseSet)

    @Query("DELETE FROM exerciseSet_data_table WHERE exerciseSet_id = :exerciseSetId")
    suspend fun deleteExerciseSet(exerciseSetId: Int)

    @Query("SELECT * FROM exerciseSet_data_table")
    fun getAllExerciseSets(): LiveData<List<ExerciseSet>>

    @Query("SELECT exerciseSet_data_table.* FROM exerciseSet_data_table\n" +
            "INNER JOIN WorkoutExerciseCrossRef ON exerciseSet_exerciseId = exercise_id\n" +
            "WHERE workout_id = :workoutId AND exercise_id = :exerciseId AND " +
            "exerciseSet_day = :day AND exerciseSet_month = :month AND exerciseSet_year = :year")
    fun getExerciseSetsByExerciseWorkoutDate(
        exerciseId: Int,
        workoutId: Int,
        day: Int,
        month: Int,
        year: Int
    ): LiveData<List<ExerciseSet>>

    @Query("SELECT exerciseSet_data_table.* FROM exerciseSet_data_table\n" +
            "INNER JOIN WorkoutExerciseCrossRef ON exerciseSet_data_table.exerciseSet_exerciseId = " +
            "WorkoutExerciseCrossRef.exercise_id\n" +
            "WHERE workout_id = :workoutId AND exercise_id = :exerciseId\n" +
            "ORDER BY exerciseSet_id DESC")
    fun getExerciseSetsByExerciseWorkout(
        exerciseId: Int,
        workoutId: Int
    ): List<ExerciseSet>



    /*********************************************/
    /**********         CROSSREF        **********/
    /*********************************************/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef)

    @Query("DELETE FROM WorkoutExerciseCrossRef WHERE workout_id = :workoutId AND exercise_id = :exerciseId")
    suspend fun deleteWorkoutExerciseCrossRef(workoutId: Int, exerciseId: Int)

    @Update
    suspend fun updateWorkoutExerciseCrossRef(crossRef: WorkoutExerciseCrossRef)

    @Query("SELECT * FROM exercises_data_table WHERE exercise_id = :exerciseId")
    fun getWorkoutsOfExercise(exerciseId: Int): LiveData<ExerciseWithWorkouts>

    @Query("SELECT * FROM workouts_data_table WHERE workout_id = :workoutId")
    fun getExercisesOfWorkout(workoutId: Int): LiveData<WorkoutWithExercises>

    @Query("SELECT * FROM WorkoutExerciseCrossRef WHERE position = :position")
    fun getWorkoutExerciseCrossRefByPosition(position: Int): WorkoutExerciseCrossRef

    @Query("SELECT exercises_data_table.*\n" +
            "FROM WORKOUTEXERCISECROSSREF \n" +
            "INNER JOIN exercises_data_table\n" +
            "ON WORKOUTEXERCISECROSSREF.exercise_id = exercises_data_table.exercise_id\n" +
            "WHERE WORKOUTEXERCISECROSSREF.workout_id = :workoutId\n" +
            "ORDER BY position ASC")
    fun getExercisesOfWorkoutOrderedByPosition(workoutId: Int): LiveData<List<Exercise>>
}