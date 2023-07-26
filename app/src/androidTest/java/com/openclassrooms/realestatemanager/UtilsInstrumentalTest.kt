@file:Suppress("DEPRECATION")

package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import com.openclassrooms.realestatemanager.utils.Utils
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UtilsInstrumentalTest {

    private lateinit var context: Context
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCapabilities: NetworkCapabilities
    private lateinit var networkInfo: NetworkInfo
    private var activeNetwork: Network? = null

    @Before
    fun setUp()  {
        context = mockk()
        connectivityManager = mockk()
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager

        networkCapabilities = mockk()



        networkInfo = mockk()
        every { connectivityManager.activeNetworkInfo } returns networkInfo
    }

    @Test
    fun isInternetAvailableWithWifiTrueTest() {
        // Given
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { networkInfo.type } returns ConnectivityManager.TYPE_WIFI

        every { networkInfo.isConnected } returns true
        activeNetwork = mockk()
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities

        // When
        val isInternetAvailable = Utils.isInternetAvailable(context)

        // Then
        Assert.assertTrue(isInternetAvailable)
    }

    @Test
    fun isInternetAvailableWithWifiFalseTest() {
        // Given
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns false
        every { networkInfo.type } returns ConnectivityManager.TYPE_WIFI

        every { networkInfo.isConnected } returns false
        activeNetwork = null
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities

        // When
        val isInternetAvailable = Utils.isInternetAvailable(context)

        // Then
        Assert.assertFalse(isInternetAvailable)
    }

    @Test
    fun isInternetAvailableWithCellularTrueTest() {
        // Given
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { networkInfo.type } returns ConnectivityManager.TYPE_MOBILE

        every { networkInfo.isConnected } returns true
        activeNetwork = mockk()
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities

        // When
        val isInternetAvailable = Utils.isInternetAvailable(context)

        // Then
        Assert.assertTrue(isInternetAvailable)
    }

    @Test
    fun isInternetAvailableWithCellularFalseTest() {
        // Given
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns false
        every { networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
        every { networkInfo.type } returns ConnectivityManager.TYPE_MOBILE

        every { networkInfo.isConnected } returns false
        activeNetwork = null
        every { connectivityManager.activeNetwork } returns activeNetwork
        every { connectivityManager.getNetworkCapabilities(activeNetwork) } returns networkCapabilities

        // When
        val isInternetAvailable = Utils.isInternetAvailable(context)

        // Then
        Assert.assertFalse(isInternetAvailable)
    }

}