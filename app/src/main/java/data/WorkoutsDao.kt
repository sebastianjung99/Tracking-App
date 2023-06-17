package data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutsDao {
    @Insert
    suspend fun insertWorkout(workouts: Workouts)

    @Update
    suspend fun updateWorkout(workouts: Workouts)

    @Delete
    suspend fun deleteWorkout(workouts: Workouts)

    @Query("SELECT * FROM workouts_data_table")
    fun getAllWorkouts():LiveData<List<Workouts>>

}