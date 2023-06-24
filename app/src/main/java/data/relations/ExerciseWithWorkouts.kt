package data.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import data.Exercise
import data.Workout

data class ExerciseWithWorkouts(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "exercise_id",
        entityColumn = "workout_id",
        associateBy = Junction(WorkoutExerciseCrossRef::class)
    )
    val workouts: List<Workout>
)