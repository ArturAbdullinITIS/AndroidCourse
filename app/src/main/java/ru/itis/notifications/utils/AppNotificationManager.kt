package ru.itis.notifications.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.itis.notifications.R
import ru.itis.notifications.domain.entities.Notification
import ru.itis.notifications.domain.entities.NotificationPriority
import ru.itis.notifications.presentation.main.MainActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val KEY_TEXT_REPLY = "key_text_reply"
        const val EXTRA_NOTIFICATION_ID = "extra_notification_id"
    }

    init {
        createNotificationChannels()
    }

    fun showNotification(notification: Notification) {
        val channelId = getChannelId(notification.priority)

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(notification.title)
            .setContentText(notification.content)
            .setPriority(mapPriorityToAndroid(notification.priority))
            .setAutoCancel(true)

        if (notification.isExpandable) {
            builder.setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notification.content)
            )
        }
        if (notification.shouldOpenApp) {
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(context.getString(R.string.key_title), notification.title)
                putExtra(context.getString(R.string.key_content), notification.content)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                notification.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.setContentIntent(pendingIntent)
        }

        if (notification.hasReplyAction) {
            val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
                .setLabel(context.getString(R.string.reply_action_label))
                .build()

            val replyIntent = Intent(context, NotificationReceiver::class.java).apply {
                action = context.getString(R.string.reply_action)
                putExtra(EXTRA_NOTIFICATION_ID, notification.id)
            }

            val replyPendingIntent = PendingIntent.getBroadcast(
                context,
                notification.id,
                replyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            val replyAction = NotificationCompat.Action.Builder(
                android.R.drawable.ic_menu_send,
                context.getString(R.string.reply_action_button),
                replyPendingIntent
            )
                .addRemoteInput(remoteInput)
                .build()

            builder.addAction(replyAction)
        }

        try {
            NotificationManagerCompat.from(context).notify(notification.id, builder.build())
            Log.d("NotificationManager", "Notification shown: id=${notification.id}, title=${notification.title}")
        } catch (e: SecurityException) {
            Log.e("NotificationManager", "Permission denied: ${e.message}")
        }
    }

    fun cancelNotification(id: Int) {
        notificationManager.cancel(id)
    }

    fun cancelAllNotifications() {
        notificationManager.cancelAll()
    }

    fun notificationExists(id: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNotifications = notificationManager.activeNotifications
            return activeNotifications.any { it.id == id }
        }
        return false
    }

    private fun getChannelId(priority: NotificationPriority): String {
        return when (priority) {
            NotificationPriority.MIN -> context.getString(R.string.channel_id_min)
            NotificationPriority.LOW -> context.getString(R.string.channel_id_low)
            NotificationPriority.MEDIUM -> context.getString(R.string.channel_id_default)
            NotificationPriority.HIGH -> context.getString(R.string.channel_id_high)
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    context.getString(R.string.channel_id_min),
                    context.getString(R.string.channel_min_priority),
                    NotificationManager.IMPORTANCE_MIN
                ),
                NotificationChannel(
                    context.getString(R.string.channel_id_low),
                    context.getString(R.string.channel_low_priority),
                    NotificationManager.IMPORTANCE_LOW
                ),
                NotificationChannel(
                    context.getString(R.string.channel_id_default),
                    context.getString(R.string.channel_default_priority),
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                NotificationChannel(
                    context.getString(R.string.channel_id_high),
                    context.getString(R.string.channel_high_priority),
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
            notificationManager.createNotificationChannels(channels)
            Log.d("NotificationManager", "Notification channels created")
        }
    }

    private fun mapPriorityToAndroid(priority: NotificationPriority): Int {
        return when (priority) {
            NotificationPriority.MIN -> NotificationCompat.PRIORITY_MIN
            NotificationPriority.LOW -> NotificationCompat.PRIORITY_LOW
            NotificationPriority.MEDIUM -> NotificationCompat.PRIORITY_DEFAULT
            NotificationPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
        }
    }
}
