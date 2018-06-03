package tech.summerly.quiet.player.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.app.NotificationCompat
import android.support.v7.graphics.Palette
import android.support.v7.graphics.Target
import androidx.core.graphics.drawable.toBitmap
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.notification.NotificationHelper
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.PlayerType
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.color
import tech.summerly.quiet.commonlib.utils.toPictureUrl
import tech.summerly.quiet.player.PlayerModule
import tech.summerly.quiet.player.R


/**
 * Utils for notify MediaStyle notification for music
 *
 *  see more at [update]
 */
internal object MusicNotification : NotificationHelper() {

    val imageSize = PlayerModule.dip(50)

    val colorDefault = color(R.color.common_color_primary_dark)

    private var music: Music? = null

    private var isPlaying: Boolean = false

    private var isFavorite: Boolean = false

    /**
     * 标识是否通知正常完成
     *
     * 如果图片还在从服务端进行加载，而是使用的默认的图片来弹出通知的话，则视为当前通知还没完成。
     */
    private var isNotifyCompleted = false

    /**
     * update current music notification
     *
     * Note: the notification will not refresh if music or player'state do not changed
     *
     * when player paused,the notification will be cancelAble for user and will stop foreground service.
     *
     * @param service the MusicPlayerService
     */
    fun update(service: MusicPlayerService) {
        val playerState = MusicPlayerManager.playerState.value
        val music = MusicPlayerManager.playingMusic.value

        if (music == null) {
            cancel(ID_NOTIFICATION_PLAY_SERVICE)
            return
        }

        val isPlaying: Boolean
        val isFav: Boolean = music.isFavorite
        val cancelAble: Boolean
        if (playerState == PlayerState.Playing || playerState == PlayerState.Preparing) {
            isPlaying = true
            cancelAble = false
        } else {
            cancelAble = true
            isPlaying = false
        }

        val isDataMatched = {
            this.music == music && this.isPlaying == isPlaying
                    && this.isFavorite == isFav
        }
        val notNeedNotify = isDataMatched() && this.isNotifyCompleted
        if (notNeedNotify) {
            return
        }

        this.music = music
        this.isPlaying = isPlaying
        this.isFavorite = isFav
        this.isNotifyCompleted = false

        val builder = NotificationCompat
                .Builder(context, ID_PLAY_SERVICE)
                .buildStep1()
                .buildStep2(music)

        //load image

        val preview = {
            val bitmap = ColorDrawable(colorDefault).toBitmap(imageSize, imageSize)
            val notification = builder.buildStep3(bitmap, colorDefault).build()
            service.notify(notification, cancelAble)
        }

        val picUrl = music.picUri?.toPictureUrl()
        if (picUrl == null) {
            preview()
            isNotifyCompleted = true
        } else {
            GlideApp.with(service).asBitmap().load(picUrl)
                    .into(object : SimpleTarget<Bitmap>(imageSize, imageSize) {
                        override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                            resource ?: return
                            val outDate = !isDataMatched()
                            if (!outDate) {
                                val palette = Palette.from(resource).clearTargets().addTarget(Target.MUTED).addTarget(Target.VIBRANT).generate()
                                val color = palette.getVibrantColor(palette.getMutedColor(colorDefault))
                                val notification = builder.buildStep3(resource, color).build()
                                service.notify(notification, cancelAble)
                                isNotifyCompleted = true
                            }
                        }

                        override fun onLoadStarted(placeholder: Drawable?) {
                            preview()
                        }
                    })
        }

    }

    private fun Service.notify(notification: Notification, cancelAble: Boolean) {
        if (cancelAble.not()) {
            startForeground(ID_NOTIFICATION_PLAY_SERVICE, notification)
        } else {
            stopForeground(false)
            notify(ID_NOTIFICATION_PLAY_SERVICE, notification)
        }
    }

    private val context: Context
        get() = PlayerModule

    private fun NotificationCompat.Builder.buildStep1(): NotificationCompat.Builder {
        setShowWhen(true)
        val mediaStyle = android.support.v4.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
        setStyle(mediaStyle)
        setSmallIcon(R.drawable.player_ic_music_note_black_24dp)
        setColorized(true)
        setContentIntent(buildContentIntent())
        return this
    }

    private fun NotificationCompat.Builder.buildStep2(music: Music): NotificationCompat.Builder {
        setContentTitle(music.title)
        setContentText(music.artistAlbumString())


        if (isFavorite) {
            addAction(R.drawable.common_ic_favorite_red_24dp, "fav", buildPlaybackAction(0))
        } else {
            addAction(R.drawable.ic_favorite_border_red_24dp, "fav", buildPlaybackAction(0))
        }

        if (MusicPlayerManager.player.playlist.type == PlayerType.NORMAL) {
            addAction(R.drawable.common_ic_skip_previous_black_24dp, "previous", buildPlaybackAction(1))
        } else {
            addAction(R.drawable.player_ic_delete_black_24dp, "delete", buildPlaybackAction(5))
        }

        if (isPlaying) {
            addAction(R.drawable.ic_pause_black_24dp, "pauseOrPlay", buildPlaybackAction(2))
        } else {
            addAction(R.drawable.ic_play_arrow_black_24dp, "pauseOrPlay", buildPlaybackAction(2))
        }

        addAction(R.drawable.common_ic_skip_next_black_24dp, "next", buildPlaybackAction(3))

        addAction(R.drawable.common_ic_close_black_24dp, "close", buildPlaybackAction(4))

        setContentIntent(buildContentIntent())

        return this
    }


    private fun NotificationCompat.Builder.buildStep3(image: Bitmap, color: Int): NotificationCompat.Builder {
        setLargeIcon(image)
        setColor(color)
        return this
    }


    private fun buildContentIntent(): PendingIntent {
        val intent = Intent(context, NotificationRouterActivity::class.java)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildPlaybackAction(which: Int): PendingIntent {
        val intent = Intent(context, MusicPlayerService::class.java)
        val action: PendingIntent
        when (which) {
            0 -> {
                intent.action = MusicPlayerService.action_like
                action = PendingIntent.getService(context, 0, intent, 0)
            }
            1 -> {
                intent.action = MusicPlayerService.action_play_previous
                action = PendingIntent.getService(context, 1, intent, 0)
            }
            2 -> {
                intent.action = MusicPlayerService.action_play_pause
                action = PendingIntent.getService(context, 2, intent, 0)
            }
            3 -> {
                intent.action = MusicPlayerService.action_play_next
                action = PendingIntent.getService(context, 3, intent, 0)
            }
            4 -> {
                intent.action = MusicPlayerService.action_exit
                action = PendingIntent.getService(context, 4, intent, 0)
            }
            5 -> {
                intent.action = MusicPlayerService.action_dislike
                action = PendingIntent.getService(context, 5, intent, 0)
            }
            else -> error("")
        }
        return action
    }


}

