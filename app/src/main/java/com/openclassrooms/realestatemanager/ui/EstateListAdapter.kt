package com.openclassrooms.realestatemanager.ui

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import java.text.DecimalFormat

class EstateListAdapter(
    private val estateListAdapterListener: EstateListAdapterListener,
    private val resources: Resources
) : ListAdapter<Estate, EstateListAdapter.ViewHolder>(ItemDiffCallback()) {

    private val decimalFormat: DecimalFormat = DecimalFormat("$#,###.00")
    private var selectedEstate: Estate? = null
    private var lastSelectedEstate: Estate? = null

    interface EstateListAdapterListener {
        fun onEstateItemClick(estate: Estate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estate_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estate = currentList[position]
        holder.type.text = estate.type.getString(resources)
        holder.city.text = estate.city
        holder.price.text = decimalFormat.format(estate.price)
        holder.itemView.setOnClickListener { estateListAdapterListener.onEstateItemClick(estate) }
        if (selectedEstate == estate) {
            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.estate_list_selected_estate, null))
            holder.price.setHintTextColor(ResourcesCompat.getColor(resources, R.color.estate_list_selected_estate_text, null))
        }
        if (lastSelectedEstate == estate) {
            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.design_default_color_background, null))
            holder.price.setHintTextColor(ResourcesCompat.getColor(resources, R.color.estate_list_price, null))
        }
    }

    class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val type: TextView = itemView.findViewById(R.id.item_estate_type)
        val city: TextView = itemView.findViewById(R.id.item_estate_city)
        val price: TextView = itemView.findViewById(R.id.item_estate_price)
    }

    private class ItemDiffCallback: DiffUtil.ItemCallback<Estate>() {
        override fun areItemsTheSame(oldItem: Estate, newItem: Estate): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Estate, newItem: Estate): Boolean {
            return oldItem == newItem
        }
    }

    fun selectedEstateColor(selectedEstate: Estate?) {
        this.lastSelectedEstate = this.selectedEstate
        this.selectedEstate = selectedEstate
        if (lastSelectedEstate != null) {
            notifyItemChanged(currentList.indexOf(lastSelectedEstate))
        }
        if (selectedEstate != null) {
            notifyItemChanged(currentList.indexOf(selectedEstate))
        }
    }

}