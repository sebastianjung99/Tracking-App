package data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BodyMetricsDao {
    @Insert
    suspend fun insertWeightTrackingRecord(weightTrackingRecord: WeightTrackingRecord)

    @Update
    suspend fun updateWeightTrackingRecord(weightTrackingRecord: WeightTrackingRecord)

    @Query("DELETE FROM weight_data_table WHERE weight_id = :weightId")
    suspend fun deleteWeightTrackingRecord(weightId: Int)

    @Query("SELECT * FROM weight_data_table")
    fun getAllWeightTrackingRecords(): LiveData<List<WeightTrackingRecord>>

}