package tech.summerly.quiet.commonlib.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.R

/**
 * author : yangbin10
 * date   : 2017/12/27
 */
open class NotificationHelper {
    companion object {
        const val ID_PLAY_SERVICE = "music_play_service"

        const val ID_NOTIFICATION_PLAY_SERVICE = 0x30312

    }

    private val context: Context
        get() = LibModule

    private val notificationManger: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelMusicService =
                    NotificationChannel(ID_PLAY_SERVICE,
                            context.getString(R.string.common_channel_name_music_play_service), NotificationManager.IMPORTANCE_LOW)

            channelMusicService.description = context.getString(R.string.common_channel_description_music_play_service)
            channelMusicService.enableLights(false)
            channelMusicService.enableVibration(false)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelMusicService)
        }
    }

    fun notify(id: Int, notification: Notification) {
        notificationManger.notify(id, notification)
    }

    fun cancel(id: Int) {
        notificationManger.cancel(id)
    }


}