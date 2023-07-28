package com.openclassrooms.realestatemanager.api

import android.database.Cursor
import android.database.MatrixCursor
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.model.Agent

class AgentList: AgentApi {

    private val agentList = listOf(
        Agent(0, "Marc Leblanc"),
        Agent(1, "Eric Dupont"),
        Agent(2, "Elise Duval"),
        Agent(3, "Henri Auger"),
    )

    override fun getAgentListLiveData(): LiveData<List<Agent>> = MutableLiveData(agentList)

    override fun getAgentListCursor(): Cursor {
        val columnNames = arrayOf("id", "name")
        val cursor = MatrixCursor(columnNames)
        agentList.forEach { agent ->
            val rowData = arrayOf(agent.id, agent.name )
            cursor.addRow(rowData)
        }
        return cursor
    }

}