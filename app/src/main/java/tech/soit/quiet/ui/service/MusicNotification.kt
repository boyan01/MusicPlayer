package tech.soit.quiet.ui.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.VisibleForTesting
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.palette.graphics.Target.MUTED
import androidx.palette.graphics.Target.VIBRANT
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import tech.soit.quiet.AppContext
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.player.playlist.Playlist
import tech.soit.quiet.utils.component.AppTask
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.support.attrValue
import tech.soit.quiet.utils.component.support.dimen
import tech.soit.quiet.utils.component.support.string
import tech.soit.quiet.utils.isFavorite
import tech.soit.quiet.utils.subTitle
import tech.soit.quiet.utils.testing.OpenForTesting


/**
 * Utils for notify MediaStyle notification for music
 *
 * see more at [update]
 */
@OpenForTesting
class MusicNotification {

    companion object {

        private const val ID_PLAY_SERVICE = "music_play_service"

        private const val ID_NOTIFICATION_PLAY_SERVICE = 0x30312


    }


    private val context: Context get() = AppContext


    private val notificationManger: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelMusicService =
                    NotificationChannel(ID_PLAY_SERVICE,
                            context.getString(R.string.app_name), NotificationManager.IMPORTANCE_LOW)

            channelMusicService.description = context.getString(R.string.app_name)
            channelMusicService.enableLights(false)
            channelMusicService.enableVibration(false)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channelMusicService)
        }
    }

    private val imageSize = dimen(R.dimen.notification_image_size).toInt()


    private val colorDefault: Int
        @ColorInt get() {
            val context: Context = AppTask.topStackActivity ?: AppContext
            return context.attrValue(R.attr.colorPrimary)
        }

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
     * @param service the tech.soit.quiet.ui.service.QuietPlayerService
     */
    fun update(service: Service) {
        checkNotification(
                onCancel = {
                    notificationManger.cancel(ID_NOTIFICATION_PLAY_SERVICE)
                },
                onNotify = { builder, cancelAble ->
                    val notification = builder.build()
                    service.notify(notification, cancelAble)
                })
    }


    @VisibleForTesting
    fun checkNotification(
            onNotify: (builder: NotificationCompat.Builder, cancelAble: Boolean) -> Unit,
            onCancel: () -> Unit
    ) {

        val playerState = MusicPlayerManager.playerState.value
        val music = MusicPlayerManager.playingMusic.value

        if (music == null) {
            onCancel()
            return
        }

        val isPlaying: Boolean
        val isFav: Boolean = music.isFavorite
        val cancelAble: Boolean
        if (playerState == IMediaPlayer.PLAYING) {
            isPlaying = true
            cancelAble = false
        } else {
            cancelAble = true
            isPlaying = false
        }

        val isDataMatched =
                this.music == music && this.isPlaying == isPlaying
                        && isFavorite == isFav
        val notNeedNotify = isDataMatched && isNotifyCompleted
        if (notNeedNotify) {
            return
        }

        this.music = music
        this.isPlaying = isPlaying
        isFavorite = isFav
        isNotifyCompleted = false

        val builder = NotificationCompat
                .Builder(context, ID_PLAY_SERVICE)
                .buildStep1()
                .buildStep2(music)

        //load image
        val preview = {
            val bitmap = ColorDrawable(colorDefault).toBitmap(imageSize, imageSize)
            builder.buildStep3(bitmap, colorDefault)
            onNotify(builder, cancelAble)
        }
        preview()

        val picUrl = music.getAlbum().getCoverImageUrl()
        if (picUrl == null) {
            isNotifyCompleted = true
        } else {
            val futureTarget = ImageLoader.with(AppContext).asBitmap()
                    .load(picUrl)
                    .submit(imageSize, imageSize)
            GlobalScope.launch {
                while (!futureTarget.isDone) {
                    delay(200)
                }
                val bitmap = try {
                    futureTarget.get()
                } catch (e: Exception) {
                    return@launch
                }
                val palette = Palette.from(bitmap).clearTargets().addTarget(MUTED).addTarget(VIBRANT).generate()
                val color = palette.getVibrantColor(palette.getMutedColor(colorDefault))
                builder.buildStep3(bitmap, color)
                onNotify(builder, cancelAble)
                isNotifyCompleted = true
            }
        }

    }


    private fun Service.notify(notification: Notification, cancelAble: Boolean) {
        if (cancelAble.not()) {
            startForeground(ID_NOTIFICATION_PLAY_SERVICE, notification)
        } else {
            stopForeground(false)
            notificationManger.notify(ID_NOTIFICATION_PLAY_SERVICE, notification)
        }
    }


    private fun NotificationCompat.Builder.buildStep1(): NotificationCompat.Builder {
        setShowWhen(true)
        val mediaStyle = androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0, 1, 2)
        setStyle(mediaStyle)
        setSmallIcon(R.drawable.ic_music_note_black_24dp)
        setColorized(true)
        setContentIntent(buildContentIntent())
        return this
    }

    private fun NotificationCompat.Builder.buildStep2(music: Music): NotificationCompat.Builder {
        setContentTitle(music.getTitle())
        setContentText(music.subTitle)


        if (isFavorite) {
            addAction(R.drawable.ic_favorite_black_24dp, string(R.string.add_to_favorite), buildPlaybackAction(0))
        } else {
            addAction(R.drawable.ic_favorite_border_black_24dp, string(R.string.remove_from_favorite), buildPlaybackAction(0))
        }

        if (MusicPlayerManager.musicPlayer.playlist.token == Playlist.TOKEN_FM) {
            addAction(R.drawable.ic_delete_black_24dp, string(R.string.move_to_trash), buildPlaybackAction(5))
        } else {
            addAction(R.drawable.ic_skip_previous_black_24dp, string(R.string.skip_to_previous), buildPlaybackAction(1))
        }

        if (isPlaying) {
            addAction(R.drawable.ic_pause_black_24dp, string(R.string.pause), buildPlaybackAction(2))
        } else {
            addAction(R.drawable.ic_play_arrow_black_24dp, string(R.string.play), buildPlaybackAction(2))
        }

        addAction(R.drawable.ic_skip_next_black_24dp, string(R.string.skip_to_next), buildPlaybackAction(3))

        addAction(R.drawable.ic_close_black_24dp, string(R.string.exit_player), buildPlaybackAction(4))

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
        val intent = Intent(context, QuietPlayerService::class.java)
        val action: PendingIntent
        when (which) {
            0 -> {
                intent.action = QuietPlayerService.action_like
                action = PendingIntent.getService(context, 0, intent, 0)
            }
            1 -> {
                intent.action = QuietPlayerService.action_play_previous
                action = PendingIntent.getService(context, 1, intent, 0)
            }
            2 -> {
                intent.action = QuietPlayerService.action_play_pause
                action = PendingIntent.getService(context, 2, intent, 0)
            }
            3 -> {
                intent.action = QuietPlayerService.action_play_next
                action = PendingIntent.getService(context, 3, intent, 0)
            }
            4 -> {
                intent.action = QuietPlayerService.action_exit
                action = PendingIntent.getService(context, 4, intent, 0)
            }
            5 -> {
                intent.action = QuietPlayerService.action_dislike
                action = PendingIntent.getService(context, 5, intent, 0)
            }
            else -> error("")
        }
        return action
    }


}

