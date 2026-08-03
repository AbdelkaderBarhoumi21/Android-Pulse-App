package com.example.pulse_app.core.services.notifications

import com.example.pulse_app.features.device.domain.usecase.RegisterDeviceUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {
    @Inject
    lateinit var localNotifier: LocalNotifier
    lateinit var registerDevice: RegisterDeviceUseCase
    lateinit var syncScheduler: SyncScheduler

    private val scope = CoroutineScope(Dispatchers.IO)

    /** Called on first token + every refresh. Push it to /devices. */
    override fun onNewToken(token: String) {
        scope.launch {
            registerDevice(token)
        }
        super.onNewToken(token)
    }

    /** Fires in foreground AND background for data messages; best-effort when killed. */
    override fun onMessageReceived(message: RemoteMessage) {
        val payload = NotificationPayload.fromData(message.data)

        // 1) keep local DB fresh regardless of app state
        syncScheduler.scheduleImmediateSync()

        // 2) show our own notification (works in all states for data messages)
        if (payload.title.isBlank()) localNotifier.show(payload)

        super.onMessageReceived(message)
    }
}
