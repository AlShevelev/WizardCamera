package com.shevelev.wizard_camera.capturing_service

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.Context
import android.os.Build
import androidx.core.content.getSystemService
import com.shevelev.wizard_camera.R
import com.shevelev.wizard_camera.core.common_entities.filter_settings.gl.GlFilterSettings
import com.shevelev.wizard_camera.core.photo_files.api.photo_shot_repository.PhotoShotRepositoryService
import org.koin.android.ext.android.inject

private const val ACTION_COMPLETE_CAPTURING = "com.shevelev.wizard_camera.capturing_service.action.COMPLETE_CAPTURING"

private const val EXTRA_PARAM = "com.shevelev.wizard_camera.capturing_service.extra.PARAM"

private const val NOTIFICATION_ID = 42137
private const val NOTIFICATION_CHANNEL_ID = "com.shevelev.wizard_camera.capturing_service.notification.CHANNEL"

@Suppress("DEPRECATION")
class PhotoShotCaptureCompleteService : IntentService("PhotoShotCaptureCompleteService") {
    private val photoShotRepository: PhotoShotRepositoryService by inject()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService<NotificationManager>()?.let { manager ->
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
                )

                manager.createNotificationChannel(notificationChannel)

                val notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle(getText(R.string.appName))
                    .setContentText(getText(R.string.capturing))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build()

                startForeground(NOTIFICATION_ID, notification)
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    @Deprecated("Deprecated in Java")
    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_COMPLETE_CAPTURING -> {
                intent.getParcelableExtra<PhotoShotCaptureCompleteParams>(EXTRA_PARAM)?.let {
                    handleCompleteCapturing(it.key, it.filter)
                }
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private fun handleCompleteCapturing(key: Long, filter: GlFilterSettings) {
        photoShotRepository.completeCapturingForService(key, filter)
    }

    companion object {
        @JvmStatic
        fun completeCapturing(context: Context, key: Long, filter: GlFilterSettings) {
            val intent = Intent(context, PhotoShotCaptureCompleteService::class.java).apply {
                action = ACTION_COMPLETE_CAPTURING
                putExtra(EXTRA_PARAM, PhotoShotCaptureCompleteParams(key, filter))
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}