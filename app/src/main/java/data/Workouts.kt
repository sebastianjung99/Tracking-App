package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts_data_table")
data class Workouts (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    val id: Int,
    @ColumnInfo(name = "workout_title")
    val title: String
)