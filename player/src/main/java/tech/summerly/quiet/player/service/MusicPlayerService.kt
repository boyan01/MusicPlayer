package tech.summerly.quiet.player.service

import android.app.Service
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.content.Intent
import android.os.IBinder
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicPlayer
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.MusicPlayerManager.player
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.observe
import tech.summerly.quiet.player.PlayerModule

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

        const val action_dislike = "dislike"

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

    override fun onCreate() {
        isRunning = true
        super.onCreate()
        lifecycleRegister.markState(Lifecycle.State.STARTED)
        playerManager.playerState.observe(this) {
            MusicNotification.update(this)
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
                MusicNotification.update(this)
                musicPlayer.destroy()
                stopSelf()
            }
            action_like -> {
            }
            action_dislike -> {

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


}