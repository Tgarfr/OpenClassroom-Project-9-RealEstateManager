package com.openclassrooms.realestatemanager.ui

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Estate

class EstateTypeSpinnerAdapter(
    private val resources: Resources
    ): BaseAdapter() {

    private val estateTypeArray: Array<Estate.Type> = Estate.Type.values()

    override fun getCount(): Int {
        return estateTypeArray.size + 1
    }

    override fun getItem(position: Int): Any? {
        return if (position == 0) {
            null
        } else {
            estateTypeArray[position]
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        view = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_type_estate_list, parent, false)

        val estateTypeTextView: TextView = view.findViewById(R.id.spinner_item_type_estate)
        if (position == 0) {
            estateTypeTextView.text = resources.getString(R.string.estate_type_not_selected)
        } else {
            estateTypeTextView.text = estateTypeArray[position - 1].getString(resources)
        }

        return view
    }

}