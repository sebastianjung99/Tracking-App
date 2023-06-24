package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "workouts_data_table")
data class Workouts (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    val id: Int,
    @ColumnInfo(name = "workout_title")
    val title: String,
    @ColumnInfo(name = "workout_exerciseList")
    val exerciseList: List<Exercises>
)