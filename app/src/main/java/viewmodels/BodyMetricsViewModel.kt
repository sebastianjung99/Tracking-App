package viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.BodyMetricsDao
import data.WeightTrackingRecord
import kotlinx.coroutines.launch

class BodyMetricsViewModel(
    private val dao: BodyMetricsDao
): ViewModel() {
    val weightTrackingRecords = dao.getAllWeightTrackingRecords()

    fun insertWeightTrackingRecord(weightTrackingRecord: WeightTrackingRecord) = viewModelScope.launch {
        dao.insertWeightTrackingRecord(weightTrackingRecord)
    }

    fun deleteWeightTrackingRecord(weightId: Int) = viewModelScope.launch {
        dao.deleteWeightTrackingRecord(weightId)
    }

    fun updateWeightTrackingRecord(weightTrackingRecord: WeightTrackingRecord) = viewModelScope.launch {
        dao.updateWeightTrackingRecord(weightTrackingRecord)
    }

}