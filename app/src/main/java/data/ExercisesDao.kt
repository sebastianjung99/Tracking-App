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

    @Query("DELETE FROM exercises_data_table WHERE exercise_id = :exercise_id")
    suspend fun deleteExercise(exercise_id: Int)

    @Query("SELECT * FROM exercises_data_table")
    fun getAllExercises():LiveData<List<Exercises>>

}