package data

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "workouts_data_table")
data class Workouts (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "workout_id")
    val id: Int,
    @ColumnInfo(name = "workout_title")
    val title: String
)

@Entity(
    indices = [
        Index(value = ["exerciseID"], unique = true)
    ]
)
data class Exercise(
    @PrimaryKey
    var exerciseId: Long? = null,
    var exerciseTitle: String
)
/* The Mapping Table
    Note also know as reference table, associative table and other names
 */

/* POJO for extracting a workout with thier list of exercises */
data class WorkoutWithExercises(
    @Embedded
    var workouts: Workouts,
    @Relation(
        entity = Exercise::class,
        parentColumn = "id",
        entityColumn = "exerciseId",
        associateBy = Junction(
            value = WorkoutExerciseMap::class,
            parentColumn = "workoutIdMap",
            entityColumn = "exerciseIdMap"
        )
    )
    var exerciseList: List<Exercise>
)

/* This is the Mapping Table that maps workouts to exercises */
@Entity(
    primaryKeys = ["workoutIdMap", "exerciseIdMap"],
    foreignKeys = [
        ForeignKey(
            entity = Workouts::class,
            parentColumns = ["id"],
            childColumns = ["workoutIdMap"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["exerciseId"],
            childColumns = ["exerciseIdMap"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)

data class WorkoutExerciseMap(
    var workoutIdMap: Long,
    @ColumnInfo(index = true) /* more efficient to have index on the 2nd column (first is indexed as first part of the Primary key) */
    var exerciseIdMap: Long
)
