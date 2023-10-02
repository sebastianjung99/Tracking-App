package adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trackingapp.databinding.ItemDataBinding

class DataAdapter(): RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

    private val dataList = ArrayList<String>()

    private lateinit var mListener : onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int, view: View, selectedData: String)
    }
    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemDataBinding.inflate(layoutInflater, parent, false)
        return DataViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.binding.apply {
            tvData.text = dataList[position]
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setList(availableData: List<String>) {
        dataList.clear()
        dataList.addAll(availableData)
    }

    inner class DataViewHolder(
        val binding: ItemDataBinding,
        listener: onItemClickListener
    ): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, it, dataList[bindingAdapterPosition])
            }
        }
    }

}