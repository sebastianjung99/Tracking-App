package data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_data_table")
data class WeightTrackingRecord(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "weight_id")
    val weightId: Int,

    @ColumnInfo(name = "weight_day")
    val weightDay: Int,
    @ColumnInfo(name = "weight_month")
    val weightMonth: Int,
    @ColumnInfo(name = "weight_year")
    val weightYear: Int,

    @ColumnInfo(name = "weight_weight")
    val weightWeight: Float,
    @ColumnInfo(name = "weight_bodyfat")
    val weightBodyFat: Float
)
