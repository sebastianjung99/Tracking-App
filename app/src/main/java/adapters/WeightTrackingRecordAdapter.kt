package adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.R
import com.example.trackingapp.databinding.ItemWeightTrackingRecordBinding
import data.WeightTrackingRecord
import java.time.LocalDate
import java.time.format.DateTimeFormatter
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

            val date = LocalDate.of(
                weightTrackingRecordList[position].weightYear,
                weightTrackingRecordList[position].weightMonth,
                weightTrackingRecordList[position].weightDay
                ).format(DateTimeFormatter.ofPattern("dd. MMM"))

            val weight = weightTrackingRecordList[position].weightWeight
            val bodyFat = weightTrackingRecordList[position].weightBodyFat
            var ratio = weight / bodyFat

            // round to 2 decimals
            ratio = (ratio * 100.0).roundToInt() / 100.0

            tvWeightTrackingRecordDate.text = date.toString()
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