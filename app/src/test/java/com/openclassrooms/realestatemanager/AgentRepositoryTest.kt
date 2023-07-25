package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class AgentRepositoryTest {

    private val agentRepository = AgentRepository()

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun getAgentListLiveDataTest() {
        // Given

        // When
        val actualAgentListLiveData = agentRepository.getAgentListLiveData()

        // Then
        val actualAgentList = LiveDataTestUtils.getValue(actualAgentListLiveData)
        Assert.assertFalse(actualAgentList.isNullOrEmpty())
        Assert.assertTrue(actualAgentList?.get(0) is Agent)
    }

}