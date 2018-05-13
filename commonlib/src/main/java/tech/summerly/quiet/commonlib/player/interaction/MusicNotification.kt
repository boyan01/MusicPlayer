package tech.summerly.quiet.commonlib.player.interaction

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.support.v7.graphics.Palette
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.launcher.ARouter
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.notification.NotificationHelper
import tech.summerly.quiet.commonlib.player.MusicPlayerManager.player
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log


internal object MusicNotification : NotificationHelper() {

    private val context: Context
        get() = LibModule

    private fun createNotification(
            type: MusicType,
            title: String,
            subtitle: String,
            largeIcon: Bitmap,
            isFav: Boolean,
            isPlaying: Boolean
    ): Notification {
        val color = Palette.from(largeIcon).generate().mutedSwatch?.rgb ?: Color.WHITE
        //music notification style
        val style = android.support.v4.media.app.NotificationCompat.MediaStyle()
                .setShowCancelButton(true).setShowActionsInCompactView(0, 1, 2)
        return NotificationCompat.Builder(context, NotificationHelper.ID_PLAY_SERVICE)
                .setShowWhen(true)
                .setStyle(style)
                .setColor(color)
                .setLargeIcon(largeIcon)
                .setSmallIcon(R.drawable.common_icon_notification_default)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setColorized(true)
                .let {
                    // when type is FM , it can not be have pre button
                    if (type == MusicType.NETEASE_FM) {
                        if (isFav) {
                            it.addAction(R.drawable.common_ic_favorite_red_24dp, "fav", buildPlaybackAction(0, type))
                        } else {
                            it.addAction(R.drawable.ic_favorite_border_red_24dp, "fav", buildPlaybackAction(0, type))
                        }
                    } else {
                        it.addAction(R.drawable.common_ic_skip_previous_black_24dp, "prev", buildPlaybackAction(0, type))
                    }
                    it
                }
                .addAction(R.drawable.common_ic_pause_circle_outline_black_24dp.takeIf { isPlaying }
                        ?: R.drawable.common_ic_play_circle_outline_black_24dp,
                        "pauseOrPlay", buildPlaybackAction(1, type))
                .addAction(R.drawable.common_ic_skip_next_black_24dp, "next", buildPlaybackAction(2, type))
                .addAction(R.drawable.common_ic_close_black_24dp, "close", buildPlaybackAction(3, type))
                .setContentIntent(buildContentIntent(type))
                .build()
    }

    private fun buildContentIntent(type: MusicType): PendingIntent? {
        val stackBuilder = TaskStackBuilder.create(context)
        val pp = when (type) {
            MusicType.NETEASE_FM -> ARouter.getInstance().build("/netease/fm")
            MusicType.NETEASE, MusicType.LOCAL -> ARouter.getInstance().build("/netease/player")
        }
        try {
            LogisticsCenter.completion(pp)
            stackBuilder.addNextIntent(Intent(context, pp.destination))
        } catch (e: Exception) {
            log(LoggerLevel.ERROR) { e.printStackTrace();"MusicPlayer for $type do not match！" }
        }
        if (stackBuilder.intentCount <= 0) {
            return null
        }
        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildPlaybackAction(which: Int, type: MusicType): PendingIntent {
        val intent = Intent(context, MusicPlayerService::class.java)
        val action: PendingIntent
        when (which) {
            0 -> {
                if (type == MusicType.NETEASE_FM) {
                    intent.action = MusicPlayerService.action_like
                } else {
                    intent.action = MusicPlayerService.action_play_previous
                }
                action = PendingIntent.getService(context, 0, intent, 0)
            }
            1 -> {
                intent.action = MusicPlayerService.action_play_pause
                action = PendingIntent.getService(context, 1, intent, 0)
            }
            2 -> {
                intent.action = MusicPlayerService.action_play_next
                action = PendingIntent.getService(context, 2, intent, 0)
            }
            3 -> {
                intent.action = MusicPlayerService.action_exit
                action = PendingIntent.getService(context, 3, intent, 0)
            }
            else -> error("")
        }
        return action
    }

    operator fun invoke(music: Music, artwork: Bitmap): Notification = with(music) {
        val currentIsPlaying: Boolean = player.mediaPlayer.getPlayerState() == PlayerState.Playing
        createNotification(type, title, artistAlbumString(), artwork, isFavorite, currentIsPlaying)
    }
}

