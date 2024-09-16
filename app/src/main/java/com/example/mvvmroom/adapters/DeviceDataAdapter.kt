package com.example.mvvmroom.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmroom.R
import com.example.mvvmroom.databinding.ItemLayoutBinding
import com.example.mvvmroom.room.entity.DeviceData
import com.example.mvvmroom.room.entity.LocalDeviceData

class DeviceDataAdapter(
    private val context: Context,
    private val deviceModelList: ArrayList<LocalDeviceData>
) : RecyclerView.Adapter<DeviceDataAdapter.ViewHolder>() {
    private var expandedPosition = -1

    inner class ViewHolder(val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setData(holder, position)
    }

    override fun getItemCount() = deviceModelList.size

    private fun setData(holder: ViewHolder, position: Int) = with(holder.binding) {
        val data = deviceModelList[position]
        var text = ""
        deviceName.text = data.name
        if (data.data != null){
            text = formatDeviceData(data.data)
        } else {
            text = "No data available"
        }
        tvSpecification.text = text

        val isExpanded = position == expandedPosition
        expandedView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        deviceName.setCompoundDrawablesWithIntrinsicBounds(0,0, if (isExpanded) R.drawable.baseline_arrow_drop_up_24 else R.drawable.baseline_arrow_drop_down_24, 0)
        cardLayout.setOnClickListener {
            if (isExpanded) {
                expandedPosition = -1
            } else {
                val previousExpandedPosition = expandedPosition
                expandedPosition = position
                notifyItemChanged(previousExpandedPosition)
            }
            notifyItemChanged(position)
        }
    }

    fun formatDeviceData(deviceData: DeviceData): String {

        val stringBuilder = StringBuilder()
        deviceData.color?.let { stringBuilder.append("Color: $it\n") }
        deviceData.capacity?.let { stringBuilder.append("Capacity: $it\n") }
        deviceData.capacityGB?.let { stringBuilder.append("CapacityGB: $it\n") }
        deviceData.price?.let { stringBuilder.append("Price: $it\n") }
        deviceData.year?.let { stringBuilder.append("Year: $it\n") }
        deviceData.cpuModel?.let { stringBuilder.append("CPU Model: $it\n") }
        deviceData.hardDiskSize?.let { stringBuilder.append("Hard Disk Size: $it\n") }
        deviceData.strapColour?.let { stringBuilder.append("Strap Colour: $it\n") }
        deviceData.caseSize?.let { stringBuilder.append("Case Size: $it\n") }
        deviceData.description?.let { stringBuilder.append("Description: $it\n") }
        deviceData.screenSize?.let { stringBuilder.append("Screen Size: $it\n") }
        deviceData.generation?.let { stringBuilder.append("Generation: $it\n") }

        return stringBuilder.toString().trim()
    }
}