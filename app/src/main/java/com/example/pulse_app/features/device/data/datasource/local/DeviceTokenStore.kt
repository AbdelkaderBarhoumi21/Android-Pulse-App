package com.example.pulse_app.features.device.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("device_prefs")
@Singleton
class DeviceTokenStore @Inject constructor(@ApplicationContext private val ctx: Context) {
    private val idKey = stringPreferencesKey("device_id")
    private val tokenKey = stringPreferencesKey("fcm_token")

    suspend fun save(id:String,token:String) = ctx.dataStore.edit {
        it[idKey] = id;
        it[tokenKey] = token
    }

    suspend fun deviceId ():String? = ctx.dataStore.data.map { it[idKey] }.first()
    suspend fun token():String? = ctx.dataStore.data.map { it[tokenKey]}.first()
}