package com.openclassrooms.realestatemanager.repository

import android.database.Cursor
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.api.AgentApi
import com.openclassrooms.realestatemanager.model.Agent

class AgentRepository(private val agentApi: AgentApi) {

    fun getAgentListLiveData(): LiveData<List<Agent>> = agentApi.getAgentListLiveData()

    fun getAgentListCursor(): Cursor = agentApi.getAgentListCursor()

}