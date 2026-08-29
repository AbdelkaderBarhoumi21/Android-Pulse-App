package com.example.pulse_app.features.device.data.repository

import com.example.pulse_app.core.result.AppResult
import com.example.pulse_app.features.device.data.datasource.local.DeviceTokenStore
import com.example.pulse_app.features.device.data.datasource.remote.DeviceApi
import com.example.pulse_app.features.device.data.datasource.remote.dto.DeviceDto
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceRepositoryImplTest {
    private val api = mockk<DeviceApi>()
    private val store = mockk<DeviceTokenStore>(relaxed = true)
    private val repo = DeviceRepositoryImpl(api, store, UnconfinedTestDispatcher())

    @Test
    fun `register persists id and token locally`() = runTest {
        coEvery { api.register(any()) } returns
                DeviceDto("dev-1", "tok", "android", "t", "t")
        
        val result = repo.registerToken("tok")
        
        assertTrue(result is AppResult.Success)
        coVerify { store.save("dev-1", "tok") }
    }
}
