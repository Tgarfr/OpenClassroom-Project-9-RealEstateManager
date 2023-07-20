package com.openclassrooms.realestatemanager.ui

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.model.Agent

class EstateAgentSpinnerAdapter(
    private val agentList: List<Agent>,
    private val resources: Resources
    ): BaseAdapter() {

    override fun getCount(): Int {
        return agentList.size + 1
    }

    override fun getItem(position: Int): Any? {
        return if (position == 0) {
            null
        } else {
            agentList[position]
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view  = convertView ?: LayoutInflater.from(parent?.context).inflate(R.layout.item_agent_estate_list, parent, false)

        val estateAgentTextView: TextView = view.findViewById(R.id.spinner_item_estate_agent)
        if (position == 0) {
            estateAgentTextView.text = resources.getString(R.string.estate_agent_not_selected)
        } else {
            estateAgentTextView.text = agentList[position - 1].name
        }

        return view
    }

}