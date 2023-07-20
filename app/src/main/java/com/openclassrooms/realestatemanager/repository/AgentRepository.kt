package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.model.Agent

class AgentRepository {

    private val agentList = listOf(
        Agent(0, "Marc Leblanc"),
        Agent(1, "Eric Dupont"),
        Agent(2, "Elise Duval"),
        Agent(3, "Henri Auger"),
    )

    fun getAgentListLiveData(): LiveData<List<Agent>> = MutableLiveData(agentList)

}