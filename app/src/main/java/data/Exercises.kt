package data

data class Exercises (
    val title: String,
    val setsList: MutableList<ExerciseSet>,
    var isExpendable: Boolean = false
)