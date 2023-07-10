package com.openclassrooms.realestatemanager.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Picture

class EstatePicturesAdapter(
    private val context: Context,
    private val estatePicturesAdapterListener: EstatePicturesAdapterListener?
) : ListAdapter<Picture, EstatePicturesAdapter.ViewHolder>(ItemDiffCallback()) {

    interface EstatePicturesAdapterListener {
        fun clickOnPicture(picture: Picture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_picture_estate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val picture = currentList[position]
        Glide.with(context)
            .load(picture.getUri())
            .centerCrop()
            .into(holder.picture)
        holder.picture.contentDescription = picture.description
        holder.itemView.setOnClickListener { estatePicturesAdapterListener?.clickOnPicture(picture) ?: return@setOnClickListener }
    }

    class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val picture: ImageView = itemView.findViewById(R.id.sheet_estate_picture)
    }

    private class ItemDiffCallback: DiffUtil.ItemCallback<Picture>() {
        override fun areItemsTheSame(oldItem: Picture, newItem: Picture): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Picture, newItem: Picture): Boolean {
            return oldItem == newItem
        }
    }

}