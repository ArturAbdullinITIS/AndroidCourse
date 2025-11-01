package ru.itis.notifications.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.RemoteInput
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.itis.notifications.domain.usecases.AddMessageUseCase
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var addMessageUseCase: AddMessageUseCase

    @Inject
    lateinit var notificationManager: AppNotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AppNotificationManager.REPLY_ACTION) {
            val remoteInput = RemoteInput.getResultsFromIntent(intent)
            if (remoteInput != null) {
                val replyText = remoteInput.getCharSequence(
                    AppNotificationManager.KEY_TEXT_REPLY
                )?.toString()

                if (!replyText.isNullOrBlank()) {
                    Log.d("NotificationReceiver", "Reply received: $replyText")

                    CoroutineScope(Dispatchers.IO).launch {
                        addMessageUseCase(replyText)
                    }

                    val notificationId = intent.getIntExtra(
                        AppNotificationManager.EXTRA_NOTIFICATION_ID,
                        -1
                    )
                    if (notificationId != -1) {
                        notificationManager.cancelNotification(notificationId)
                    }
                }
            }
        }
    }
}
