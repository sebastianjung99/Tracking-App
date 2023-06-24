package data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WorkoutsDao {
    @Insert
    fun insert(workouts: Workouts): Long
    @Insert
    fun insert(exercise: Exercise): Long
    @Insert
    fun insert(workoutExerciseMap: WorkoutExerciseMap): Long

    @Update
    suspend fun updateWorkout(workouts: Workouts)

    @Query("DELETE FROM workouts_data_table WHERE workout_id = :workouts_id")
    suspend fun deleteWorkout(workouts_id: Int)

    @Query("SELECT * FROM workouts_data_table")
    fun getAllWorkouts():LiveData<List<Workouts>>

    /* Query for retrieving the Person and their hobbies if they have hobbies according to the provided list of hobbyId's */
    @Query("SELECT DISTINCT workouts_data_table.* FROM workouts_data_table JOIN personHobbyMap ON person.id = personHobbyMap.personIdMap JOIN hobby ON personHobbyMap.hobbyIdMap = hobby.hobbyId WHERE hobbyId IN(:hobbyIdList);")
    fun getPersonsWithHobbiesIfHobbiesInListOfHobbyIds(hobbyIdList: List<Long>): List<PersonWithHobbies>

    /* Query for retrieving the Person and their hobbies if they have hobbies according to the provided list of hobby names's */
    @Query("SELECT DISTINCT person.* FROM person JOIN personHobbyMap ON person.id = personHobbyMap.personIdMap JOIN hobby ON personHobbyMap.hobbyIdMap = hobby.hobbyId WHERE hobbyName IN(:hobbyNameList);")
    fun getPersonsWithHobbiesIfHobbiesInListOfHobbyNames(hobbyNameList: List<String>): List<PersonWithHobbies>


}