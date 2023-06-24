package data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import data.Exercise
import data.Workout

data class WorkoutWithExercises(
    @Embedded val workout: Workout,
    @Relation(
        parentColumn = "workout_id",
        entityColumn = "exercise_id",
        associateBy = Junction(WorkoutExerciseCrossRef::class)
    )
    val exercises: List<Exercise>
)
