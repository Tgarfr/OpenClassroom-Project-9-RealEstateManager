package com.openclassrooms.realestatemanager.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate
import com.openclassrooms.realestatemanager.model.EstateItem
import java.text.DecimalFormat

class EstateListAdapter(
    private val estateListAdapterListener: EstateListAdapterListener,
    private val context: Context,
) : ListAdapter<EstateItem, EstateListAdapter.ViewHolder>(ItemDiffCallback()) {

    private val decimalFormat: DecimalFormat = DecimalFormat("$#,###.00")
    private var selectedEstateId: Long? = null

    interface EstateListAdapterListener {
        fun onEstateItemClick(estate: Estate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_estate_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estateItem = currentList[position]
        val estate = estateItem.estate
        holder.type.text = estate.type.getString(context.resources)
        holder.city.text = estate.city
        holder.price.text = decimalFormat.format(estate.price)
        holder.itemView.setOnClickListener { estateListAdapterListener.onEstateItemClick(estate) }

        if (selectedEstateId == estateItem.estate.id) {
            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.estate_list_selected_estate, null))
            holder.price.setTextColor(ResourcesCompat.getColor(context.resources, R.color.estate_list_selected_estate_text, null))
        } else {
            holder.itemView.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.design_default_color_background, null))
            holder.price.setTextColor(ResourcesCompat.getColor(context.resources, R.color.estate_list_price, null))
        }

        if (estateItem.pictureUri != null) {
            Glide.with(context)
                .load(estateItem.pictureUri)
                .centerCrop()
                .into(holder.picture)
        } else {
            Glide.with(context)
                .load(ContextCompat.getDrawable(context, R.drawable.icon_estate_picture_default))
                .centerCrop()
                .into(holder.picture)
        }

    }

    class ViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val type: TextView = itemView.findViewById(R.id.item_estate_type)
        val city: TextView = itemView.findViewById(R.id.item_estate_city)
        val price: TextView = itemView.findViewById(R.id.item_estate_price)
        val picture: ImageView = itemView.findViewById(R.id.item_estate_picture)
    }

    private class ItemDiffCallback: DiffUtil.ItemCallback<EstateItem>() {
        override fun areItemsTheSame(oldItem: EstateItem, newItem: EstateItem): Boolean {
            return oldItem.estate.id == newItem.estate.id
        }
        override fun areContentsTheSame(oldItem: EstateItem, newItem: EstateItem): Boolean {
            return oldItem == newItem
        }
    }

    fun selectedEstate(estateId: Long) {
        notifyItemChanged(currentList.indexOfFirst{ estateItem -> estateItem.estate.id == selectedEstateId})
        selectedEstateId = estateId
        notifyItemChanged(currentList.indexOfFirst{ estateItem -> estateItem.estate.id == estateId})
    }

}