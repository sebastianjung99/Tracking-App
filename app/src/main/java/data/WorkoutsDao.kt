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

    @Query("DELETE FROM workouts_data_table WHERE workout_id = :workoutId")
    suspend fun deleteWorkout(workoutId: Int)

    @Query("SELECT * FROM workouts_data_table WHERE workout_id = :workoutId")
    fun getWorkout(workoutId: Int): Workouts

    @Query("SELECT workout_exerciseList FROM workouts_data_table WHERE workout_id = :workoutId")
    fun getExercisesByWorkoutId(workoutId: Int): Exercises

    @Query("SELECT * FROM workouts_data_table")
    fun getAllWorkouts():LiveData<List<Workouts>>

}