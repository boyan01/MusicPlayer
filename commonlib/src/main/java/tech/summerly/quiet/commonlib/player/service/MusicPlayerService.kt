package tech.summerly.quiet.commonlib.player.service

import android.app.Service
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.content.Intent
import android.os.IBinder
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.notification.NotificationHelper
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.playlist.PlaylistPlayer
import tech.summerly.quiet.commonlib.utils.LoggerLevel
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

        const val action_play_previous = "previous"

        const val action_play_pause = "play_pause"

        const val action_play_next = "next"

        const val action_exit = "exit"

        const val action_like = "like"

        private var isRunning: Boolean = false

        fun start(context: Context = LibModule) {
            if (!isRunning) {
                context.startService(Intent(context, MusicPlayerService::class.java))
            } else {
                log(LoggerLevel.DEBUG) {
                    "wo do not need to start music player service ," +
                            "because it is already running..."
                }
            }
        }
    }

    private val playerManager: MusicPlayerManager
        get() = MusicPlayerManager

    private val musicPlayer: PlaylistPlayer
        get() = MusicPlayerManager.musicPlayer()

    private val currentPlaying: Music?
        get() = musicPlayer.current

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
                musicPlayer.exit()
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

    // to holder the instance of player
    private var instanceHolder: PlaylistPlayer? = null

    private fun bindPlayerToService() {
        instanceHolder = musicPlayer
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegister.markState(Lifecycle.State.DESTROYED)
        musicPlayer.exit()
        isRunning = false
    }
}