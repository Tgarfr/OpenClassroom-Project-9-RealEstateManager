package com.openclassrooms.realestatemanager.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object LiveDataTestUtils3rdValue {

    private var count = 0

    @Suppress("unchecked", "UNCHECKED_CAST")
    fun <T> getValue(liveData: LiveData<T>): T? {

        val data =  arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)

        val observer: Observer<T> = object : Observer<T> {
            override fun onChanged(value: T) {
                count++
                if (count == 3) {
                    data[0] = value
                    latch.countDown()
                }
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        liveData.removeObserver(observer)
        return data[0] as T
    }
}