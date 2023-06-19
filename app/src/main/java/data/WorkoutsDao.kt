package data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutsDao {
    @Insert
    suspend fun insertWorkout(workouts: Workouts)

    @Update
    suspend fun updateWorkout(workouts: Workouts)

    @Query("DELETE FROM workouts_data_table WHERE workout_id = :workouts_id")
    suspend fun deleteWorkout(workouts_id: Int)

    @Query("SELECT * FROM workouts_data_table")
    fun getAllWorkouts():LiveData<List<Workouts>>

}