package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises_data_table")
data class Exercises (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    val id: Int,
    @ColumnInfo(name = "exercise_title")
    val title: String,
    @ColumnInfo(name = "exercise_includedInWorkoutId")
    val includedInWorkoutId: Int
)