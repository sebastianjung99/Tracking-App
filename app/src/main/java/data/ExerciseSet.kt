package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exerciseSet_data_table")
data class ExerciseSet(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exerciseSet_id")
    val exerciseSetId: Int,
    @ColumnInfo(name = "exerciseSet_exerciseId")
    val exerciseId: Int,

    // exerciseSetId if dropset, else 0
    @ColumnInfo(name = "exerciseSet_dropSetOf")
    val dropSetOfSetId: Long,
    // setNumber is 0 if dropset
    @ColumnInfo(name = "exerciseSet_setNumber")
    val setNumber: Int,
    @ColumnInfo(name = "exerciseSet_repetitions")
    val repetitions: Int,
    @ColumnInfo(name = "exerciseSet_weight")
    val weight: Int,
    @ColumnInfo(name = "exerciseSet_Note")
    val note: String,

    @ColumnInfo(name = "exerciseSet_day")
    val weightDay: Int,
    @ColumnInfo(name = "exerciseSet_month")
    val weightMonth: Int,
    @ColumnInfo(name = "exerciseSet_year")
    val weightYear: Int,
)
