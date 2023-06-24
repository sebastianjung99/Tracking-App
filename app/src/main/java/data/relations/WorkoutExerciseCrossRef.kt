package data.relations

import androidx.room.Entity

@Entity(primaryKeys = ["workout_id", "exercise_id"])
data class WorkoutExerciseCrossRef(
    val workout_id: Int,
    val exercise_id: Int,
    val position: Int
)