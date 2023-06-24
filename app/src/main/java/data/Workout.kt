package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts_data_table")
data class Workout (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    val workoutId: Int,
    @ColumnInfo(name = "workout_title")
    val workoutTitle: String
)