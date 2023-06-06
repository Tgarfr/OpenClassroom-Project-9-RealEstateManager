package com.openclassrooms.realestatemanager.ui

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import java.text.DecimalFormat

class EstateListAdapter(
    private val resources: Resources
) : ListAdapter<Estate, EstateListAdapter.ViewHolder>(ItemDiffCallback()) {

    private val decimalFormat: DecimalFormat = DecimalFormat("$#,###.00")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estate = currentList[position]
        holder.type.text = getEstateTypeString(estate.type)
        holder.city.text = estate.city
        holder.price.text = decimalFormat.format(estate.price)
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
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

    private fun getEstateTypeString(estateType: Estate.Type): String {
        return when (estateType) {
            Estate.Type.FLAT -> resources.getString(R.string.estate_type_flat)
            Estate.Type.HOUSE -> resources.getString(R.string.estate_type_house)
            Estate.Type.DUPLEX -> resources.getString(R.string.estate_type_duplex)
            Estate.Type.PENTHOUSE -> resources.getString(R.string.estate_type_penthouse)
        }
    }
}