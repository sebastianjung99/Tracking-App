package data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Workouts::class, Exercises::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class TrackingAppDatabase: RoomDatabase() {
    abstract fun workoutsDao(): WorkoutsDao
    abstract fun exercisesDao(): ExercisesDao

    companion object {
        @Volatile
        private var INSTANCE: TrackingAppDatabase? = null
        fun getInstance(context: Context): TrackingAppDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TrackingAppDatabase::class.java,
                        "trackingApp_database"
                    ).allowMainThreadQueries().build()
                }
                return instance
            }
        }
    }
}