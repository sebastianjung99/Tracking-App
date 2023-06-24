package data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ExercisesDao {
    @Insert
    suspend fun insertExercise(exercise: Exercise): Long

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Query("DELETE FROM exercises_data_table WHERE exercise_id = :exerciseId")
    suspend fun deleteExercise(exerciseId: Int)

    @Query("SELECT * FROM exercises_data_table")
    fun getAllExercises(): LiveData<List<Exercise>>

}