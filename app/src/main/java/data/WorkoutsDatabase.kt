package data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Workouts::class],
    version = 1
)
abstract class WorkoutsDatabase: RoomDatabase() {
    abstract fun workoutsDao(): WorkoutsDao
    companion object {
        @Volatile
        private var INSTANCE: WorkoutsDatabase? = null
        fun getInstance(context: Context): WorkoutsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        WorkoutsDatabase::class.java,
                        "workouts_database"
                    ).build()
                }
                return instance
            }
        }
    }
}