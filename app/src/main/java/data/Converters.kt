package data

import androidx.room.TypeConverter
import com.google.gson.Gson

class Converters {
    @TypeConverter
    fun intListToJson(value: List<Int>?) = Gson().toJson(value)

    @TypeConverter
    fun exercisesListToJson(value: Exercises?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToExercisesList(value: String) = Gson().fromJson(value, Exercises::class.java)
}