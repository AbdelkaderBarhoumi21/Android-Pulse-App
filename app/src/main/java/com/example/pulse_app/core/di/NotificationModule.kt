package com.example.pulse_app.core.di

import android.content.Context
import androidx.activity.contextaware.ContextAware
import com.example.pulse_app.core.services.notifications.LocalNotifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NotificationModule {
    @Provides
    @Singleton
    fun localNotifier(
        @ApplicationContext context: Context,
    ) = LocalNotifier(context)
}
