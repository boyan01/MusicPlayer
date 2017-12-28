package tech.summerly.quiet.commonlib.player

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v7.graphics.Palette
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.notification.NotificationHelper
import tech.summerly.quiet.commonlib.player.state.PlayerState
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.GlideRequest
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.observe

/**
 * author : yangbin10
 * date   : 2017/12/27
 */
class MusicPlayerService : Service(), LifecycleOwner {

    private val lifecycleRegister = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegister
    }

    companion object {

        val action_play_previous = "previous"

        val action_play_pause = "play_pause"

        val action_play_next = "next"

        val action_exit = "exit"

        val action_like = "like"

    }

    private lateinit var notificationHelper: PlayerNotificationHelper


    private val musicPlayer: BaseMusicPlayer
        get() = MusicPlayerManager.INSTANCE.getMusicPlayer()

    private val currentPlaying: Music?
        get() = musicPlayer.getPlayingMusic().value

    private val isPlaying: Boolean
        get() = musicPlayer.playerState.value == PlayerState.Playing

    override fun onCreate() {
        super.onCreate()
        log { "on created " }
        notificationHelper = PlayerNotificationHelper(this)
        lifecycleRegister.markState(Lifecycle.State.STARTED)
        musicPlayer.playerState.observe(this) {
            notification()
        }
        musicPlayer.getPlayingMusic().observe(this) {
            notification()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            action_play_previous -> {
                musicPlayer.playNext()
            }

            action_play_pause -> {
                musicPlayer.playPause()
            }

            action_play_next -> {
                musicPlayer.playNext()
            }
            action_exit -> {
                stopForeground(true)
                stopSelf()
            }
            action_like -> {
                //将歌曲标记为喜欢
                //TODO
            }
        }
        bindPlayerToService()
        return super.onStartCommand(intent, flags, startId)
    }

    // to holder the instance of player
    private var instanceHolder: BaseMusicPlayer? = null

    private fun bindPlayerToService() {
        instanceHolder = musicPlayer
    }

    /**
     *
     */
    private fun GlideRequest<Bitmap>.loadWithDefault(uri: String?, handler: (Bitmap) -> Unit) {
        val default = BitmapFactory.decodeResource(resources, R.drawable.common_icon_notification_default)
        if (uri == null) {
            handler(default)
            return
        }
        load(uri).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap?, transition: Transition<in Bitmap>?) {
                if (resource != null) {
                    handler(resource)
                } else {
                    handler(default)
                }
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                handler(default)
            }

            override fun onLoadStarted(placeholder: Drawable?) {
                handler(default)
            }
        })
    }

    /**
     * 生成一个前台通知显示当前播放的音乐信息
     * fixme
     */
    private fun notification() {
        val music = currentPlaying
        if (music == null) {
            log { "current playing is null , stop foreground." }
            stopForeground(true)
            return
        }
        log { "popup notification for music player" }
        val uri = music.picUri
        GlideApp.with(this).asBitmap().loadWithDefault(uri) {
            val playing = isPlaying
            notificationHelper.buildNotification(music, isPlaying, it)?.let {
                if (playing) {
                    startForeground(NotificationHelper.ID_NOTIFICATION_PLAY_SERVICE, it)
                } else {
                    stopForeground(false)
                    notificationHelper.notify(NotificationHelper.ID_NOTIFICATION_PLAY_SERVICE, it)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegister.markState(Lifecycle.State.DESTROYED)
        musicPlayer.destroy()
        notificationHelper.cancel(NotificationHelper.ID_NOTIFICATION_PLAY_SERVICE)
    }

    private class PlayerNotificationHelper(private val context: Context) : NotificationHelper(context) {

        fun buildNotification(music: Music,
                              isPlaying: Boolean,
                              largeIcon: Bitmap): Notification? {
            val generate = Palette.from(largeIcon).generate()
            val color = generate.mutedSwatch?.rgb ?: Color.WHITE
            return NotificationCompat.Builder(context,
                    NotificationHelper.ID_PLAY_SERVICE)
                    .setShowWhen(false)// 隐藏时间戳
                    .setStyle(android.support.v4.media.app.NotificationCompat.MediaStyle()// 设置通知Style
                            .setShowActionsInCompactView(0, 1, 2))//通知处于非展开状态时显示的按钮编号
                    .setColor(color)
                    //大图标为专辑或歌手图标
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.common_ic_skip_previous_black_24dp)
                    // Set Notification content information
                    .setContentText(music.artistAlbumString())
                    .setContentInfo(music.album.name)
                    .setContentTitle(music.title)
                    .setColorized(true)
                    // Add some playback controls
                    .addAction(R.drawable.common_ic_skip_previous_black_24dp, "prev", buildPlaybackAction(0))
                    .addAction(R.drawable.common_ic_pause_circle_outline_black_24dp.takeIf { isPlaying } ?: R.drawable.common_ic_play_circle_outline_black_24dp,
                            "pauseOrPlay", buildPlaybackAction(1))
                    .addAction(R.drawable.common_ic_skip_next_black_24dp, "next", buildPlaybackAction(2))
                    .addAction(R.drawable.common_ic_close_black_24dp, "close", buildPlaybackAction(3))
//                    .setContentIntent(let {
//                        //                        TODO()
////                        PendingIntent.getActivity(context, 0, intent, 0)
//                    })
                    .build()
        }

        private fun buildPlaybackAction(which: Int): PendingIntent {
            val intent = Intent(context, MusicPlayerService::class.java)
            val action: PendingIntent
            when (which) {
                0 -> {
                    intent.action = MusicPlayerService.action_play_previous
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

    }
}