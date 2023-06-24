package data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExercisesDao {
    @Insert
    suspend fun insertExercise(exercises: Exercises)

    @Update
    suspend fun updateExercise(exercises: Exercises)

    @Query("DELETE FROM exercises_data_table WHERE exercise_id = :exerciseId")
    suspend fun deleteExercise(exerciseId: Int)

    @Query("SELECT * FROM exercises_data_table WHERE exercise_title = :exerciseTitle")
    fun getExerciseByTitle(exerciseTitle: String): Exercises?

    @Query("SELECT * FROM exercises_data_table WHERE exercise_includedInWorkoutIDs LIKE '%' || :workoutID || '%'")
    fun getAllExercisesByWorkoutId(workoutID: Int): LiveData<List<Exercises>>

    @Query("SELECT * FROM exercises_data_table")
    fun getAllExercises(): LiveData<List<Exercises>>

}