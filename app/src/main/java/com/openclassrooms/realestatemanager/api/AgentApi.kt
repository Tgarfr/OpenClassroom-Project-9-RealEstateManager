package com.openclassrooms.realestatemanager.api

import android.database.Cursor
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.model.Agent

interface AgentApi {

    fun getAgentListLiveData(): LiveData<List<Agent>>

    fun getAgentListCursor(): Cursor

}