package data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Exercises::class],
    version = 1
)
abstract class ExercisesDatabase: RoomDatabase() {
    abstract fun exercisesDao(): ExercisesDao
    companion object {
        @Volatile
        private var INSTANCE: ExercisesDatabase? = null
        fun getInstance(context: Context): ExercisesDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExercisesDatabase::class.java,
                        "exercises_database"
                    ).build()
                }
                return instance
            }
        }
    }
}