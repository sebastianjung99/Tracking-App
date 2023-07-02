package adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.R
import com.example.trackingapp.databinding.ItemWeightTrackingRecordBinding
import data.WeightTrackingRecord
import kotlin.math.roundToInt

class WeightTrackingRecordAdapter(
): RecyclerView.Adapter<WeightTrackingRecordAdapter.WeightTrackingRecordViewHolder>() {

    private val weightTrackingRecordList = ArrayList<WeightTrackingRecord>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightTrackingRecordViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWeightTrackingRecordBinding.inflate(layoutInflater, parent, false)
        return WeightTrackingRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeightTrackingRecordViewHolder, position: Int) {
        holder.binding.apply {
            var weight = weightTrackingRecordList[position].weightWeight
            var bodyFat = weightTrackingRecordList[position].weightBodyFat
            var ratio = weight / bodyFat

            // round to 2 decimals
//            weight = (weight * 100.0).roundToInt() / 100.0
//            bodyFat = (bodyFat * 100.0).roundToInt() / 100.0
            ratio = (ratio * 100.0).roundToInt() / 100.0

//            tvWeightTrackingRecordWeight.text = "${weight.toString()} ${R.string.kg}"
            tvWeightTrackingRecordWeight.text = holder.itemView.context.getString(R.string.kgPlaceholder, weight)
            tvWeightTrackingRecordBodyFat.text =  holder.itemView.context.getString(R.string.percentSignPlaceholder, bodyFat)
            tvWeightTrackingRecordRatio.text = ratio.toString()
        }
    }

    override fun getItemCount(): Int {
        return weightTrackingRecordList.size
    }

    fun setList(weightTrackingRecords: List<WeightTrackingRecord>) {
        weightTrackingRecordList.clear()
        weightTrackingRecordList.addAll(weightTrackingRecords)
    }

    inner class WeightTrackingRecordViewHolder(val binding: ItemWeightTrackingRecordBinding): RecyclerView.ViewHolder(binding.root)

}