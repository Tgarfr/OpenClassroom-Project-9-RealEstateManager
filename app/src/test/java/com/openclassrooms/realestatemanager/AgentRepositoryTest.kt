package com.openclassrooms.realestatemanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.api.AgentApi
import com.openclassrooms.realestatemanager.model.Agent
import com.openclassrooms.realestatemanager.repository.AgentRepository
import com.openclassrooms.realestatemanager.utils.LiveDataTestUtils
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AgentRepositoryTest {

    private lateinit var agentApi: AgentApi
    private lateinit var agentRepository: AgentRepository

    @JvmField
    @Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        agentApi = mockk()
        agentRepository = AgentRepository(agentApi)
    }

    @Test
    fun getAgentListLiveDataTest() {
        // Given
        val expectedAgentList = testAgentList
        every { agentApi.getAgentListLiveData() } returns MutableLiveData(expectedAgentList)

        // When
        val actualAgentListLiveData = agentRepository.getAgentListLiveData()

        // Then
        val actualAgentList = LiveDataTestUtils.getValue(actualAgentListLiveData)
        Assert.assertEquals(expectedAgentList, actualAgentList)
    }

    private val testAgentList = listOf(
        Agent(0, "Marc Leblanc"),
        Agent(1, "Eric Dupont"),
        Agent(2, "Elise Duval"),
        Agent(3, "Henri Auger"),
    )

}