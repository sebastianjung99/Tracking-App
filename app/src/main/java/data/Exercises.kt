package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "exercises_data_table")
data class Exercises (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    val id: Int,
    @ColumnInfo(name = "exercise_title")
    val title: String,
    @ColumnInfo(name = "exercise_includedInWorkoutIDs")
    val includedInWorkoutIDs: List<Int>
)

class Converters {
    @TypeConverter
    fun listToJson(value: List<Int>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Int>::class.java).toList()
}