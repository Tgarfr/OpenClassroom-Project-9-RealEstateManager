package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.di.Injection
import com.openclassrooms.realestatemanager.provider.EstateContentProvider
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EstateContentProviderTest {

    private val context = InstrumentationRegistry.getInstrumentation().context
    private val contentResolver = context.contentResolver
    private val estateRepository = Injection.getInstance(context).estateRepository

    @Test
    fun getEstateListTest() {

        val expectedCursor = estateRepository.getEstateListCursor()

        val actualCursor = contentResolver.query(EstateContentProvider.URI_ITEM, null, null, null, null)

        Assert.assertTrue(actualCursor != null)
        actualCursor?.let {

            Assert.assertTrue(expectedCursor.count == actualCursor.count)

            val columnIndexId = expectedCursor.getColumnIndex("id")
            while (expectedCursor.moveToNext()) {
                actualCursor.moveToNext()
                Assert.assertEquals(expectedCursor.getLong(columnIndexId), actualCursor.getLong(columnIndexId))
            }

        }
    }


}