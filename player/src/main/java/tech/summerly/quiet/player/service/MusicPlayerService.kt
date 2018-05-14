package tech.summerly.quiet.player.service

import android.app.Service
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.IBinder
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.cancelChildren
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.dip
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.notification.NotificationHelper
import tech.summerly.quiet.commonlib.player.MusicPlayer
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.MusicPlayerManager.player
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.observe
import tech.summerly.quiet.player.PlayerModule
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeUnit

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

        const val action_play_previous = "previous"

        const val action_play_pause = "play_pause"

        const val action_play_next = "next"

        const val action_exit = "exit"

        const val action_like = "like"

        private var isRunning: Boolean = false

        fun start(context: Context = PlayerModule) {
            if (!isRunning) {
                context.startService(Intent(context, MusicPlayerService::class.java))
            } else {
                log(LoggerLevel.DEBUG) {
                    "wo do not need to start music MusicPlayer service ," +
                            "because it is already running..."
                }
            }
        }
    }

    private val playerManager: MusicPlayerManager
        get() = MusicPlayerManager

    private val musicPlayer get() = player

    private val currentPlaying: Music?
        get() = musicPlayer.playlist.current

    override fun onCreate() {
        isRunning = true
        super.onCreate()
        lifecycleRegister.markState(Lifecycle.State.STARTED)
        playerManager.playerState.observe(this) {
            notification()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            action_play_previous -> {
                musicPlayer.playPrevious()
            }

            action_play_pause -> {
                musicPlayer.playPause()
            }

            action_play_next -> {
                musicPlayer.playNext()
            }
            action_exit -> {
                stopForeground(true)
                MusicNotification.cancel(NotificationHelper.ID_NOTIFICATION_PLAY_SERVICE)
                musicPlayer.destroy()
                stopSelf()
            }
            action_like -> {
                //将歌曲标记为喜欢
                //TODO
                log { "mark as liked : $currentPlaying" }
            }
        }
        bindPlayerToService()
        return super.onStartCommand(intent, flags, startId)
    }

    // to holder the instance of MusicPlayer
    private var instanceHolder: MusicPlayer? = null

    private fun bindPlayerToService() {
        instanceHolder = musicPlayer
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegister.markState(Lifecycle.State.DESTROYED)
        musicPlayer.destroy()
        isRunning = false
    }


    private val imageLoaderJob: Job = Job()


    /**
     * notify a music notification
     */
    private fun notification() {
        val music = musicPlayer.playlist.current
        if (music == null) {//remove notification if current playing is null.
            stopForeground(true)
            return
        }

        val uri = music.picUri

        //to show an default image notification if uri is null
        if (uri == null) {
            val defaultBigIcon = BitmapFactory.decodeResource(resources, R.drawable.common_icon_notification_default)
            notificationInternal(music, defaultBigIcon)
            return
        }
        //load image from file
        if (uri.startsWith("file:", true)) {
            imageLoaderJob.cancelChildren()
            launch(UI, parent = imageLoaderJob) {
                val bitmap =
                        try {
                            async {
                                GlideApp.with(this@MusicPlayerService).asBitmap().load(uri).submit(dip(100), dip(100))
                                        .get(10, TimeUnit.SECONDS) //only wait 10 seconds
                            }.await()
                        } catch (cancellationException: CancellationException) {
                            throw cancellationException
                        } catch (e: Exception) {
                            log { e }
                            BitmapFactory.decodeResource(resources, R.drawable.common_icon_notification_default)
                        }
                notificationInternal(music, bitmap)
            }
            return
        }

        //load image from url
        GlideApp.with(this)
                .asBitmap()
                .onlyRetrieveFromCache(true)
                .load(uri)
                .placeholder(R.drawable.common_icon_notification_default)
                .into(object : SimpleTarget<Bitmap>(dip(100), dip(100)) {
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
}