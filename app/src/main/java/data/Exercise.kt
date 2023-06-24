package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises_data_table")
data class Exercise (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exercise_id")
    val exerciseId: Int,
    @ColumnInfo(name = "exercise_title")
    val exerciseTitle: String
)