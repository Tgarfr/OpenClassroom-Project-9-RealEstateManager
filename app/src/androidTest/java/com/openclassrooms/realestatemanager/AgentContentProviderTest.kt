package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.di.Injection
import com.openclassrooms.realestatemanager.provider.AgentContentProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AgentContentProviderTest {

    private val context = InstrumentationRegistry.getInstrumentation().context
    private val contentResolver = context.contentResolver
    private val agentRepository = Injection.getInstance(context).agentRepository

    @Test
    fun getAgentListTest() {

        val expectedCursor = agentRepository.getAgentListCursor()

        val actualCursor = contentResolver.query(AgentContentProvider.URI_ITEM, null, null, null, null)

        Assert.assertTrue(actualCursor != null)
        actualCursor?.let {

            Assert.assertTrue(expectedCursor.count == actualCursor.count)

            val columnIndexId = expectedCursor.getColumnIndex("name")
            while (expectedCursor.moveToNext()) {
                actualCursor.moveToNext()
                Assert.assertEquals(expectedCursor.getString(columnIndexId), actualCursor.getString(columnIndexId))
            }

        }
    }


}