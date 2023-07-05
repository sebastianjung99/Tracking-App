package data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import data.relations.WorkoutExerciseCrossRef

@Database(
    entities = [
        Workout::class,
        Exercise::class,
        WorkoutExerciseCrossRef::class,
        ExerciseSet::class,
        WeightTrackingRecord::class
    ],
    version = 1
)
abstract class TrackingAppDatabase: RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun bodyMetricsDao(): BodyMetricsDao

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