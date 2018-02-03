package tech.summerly.quiet.commonlib.player.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v4.app.NotificationCompat
import android.support.v7.graphics.Palette
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.notification.NotificationHelper
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.toPictureUrl


private val currentIsPlaying: Boolean
    get() = MusicPlayerManager.playerState.value == PlayerState.Playing


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
                            it.addAction(R.drawable.common_ic_favorite_border_red_24dp, "fav", buildPlaybackAction(0, type))
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
                .build()
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
        createNotification(type, title, artistAlbumString(), artwork, isFavorite, currentIsPlaying)
    }
}

/**
 * notify a music notification
 */
fun MusicPlayerService.notification() {
    val music = MusicPlayerManager.musicPlayer().corePlayer.playing
    if (music == null) {//remove notification if current playing is null.
        stopForeground(true)
        return
    }
    val uri = music.picUri?.toPictureUrl()
    GlideApp.with(this)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.common_icon_notification_default)
            .into(object : SimpleTarget<Bitmap>(dip(50), dip(50)) {
                override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                    if (resource == null) {
                        log(LoggerLevel.ERROR) {
                            "on resource ready is invoked , " +
                                    "but resource bitmap is still null!"
                        }
                        return
                    }
                    notificationInternal(music, resource)
                }

                override fun onLoadStarted(placeholder: Drawable?) {
                    if (placeholder == null) {
                        log(LoggerLevel.ERROR) { "notification place holder bitmap is null!!" }
                        return
                    }
                    if (placeholder is BitmapDrawable) {
                        notificationInternal(music, placeholder.bitmap)
                    }
                }
            })
}

private fun MusicPlayerService.notificationInternal(music: Music, bitmap: Bitmap) {
    val notification = MusicNotification(music, bitmap)
    startForeground(NotificationHelper.ID_NOTIFICATION_PLAY_SERVICE, notification)
//    if (currentIsPlaying) {
//        stopForeground(false)
//    }
}