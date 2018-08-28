package tech.soit.quiet.ui.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import tech.soit.quiet.AppContext
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.player.QuietMusicPlayer
import tech.soit.quiet.utils.component.LoggerLevel
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.testing.OpenForTesting
import tech.soit.quiet.viewmodel.MusicControllerViewModel

/**
 *
 * the player service which work on Background
 *
 *
 * author : YangBin
 * date   : 2017/12/27
 */
@OpenForTesting
class QuietPlayerService : Service(), LifecycleOwner {

    private val lifecycleRegister = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegister
    }

    companion object {

        /**
         * action skip to play previous
         */
        const val action_play_previous = "previous"

        /**
         * action play when not playing, pause when playing
         */
        const val action_play_pause = "play_pause"

        /**
         * action skip to play next
         */
        const val action_play_next = "next"

        /**
         * action close player
         */
        const val action_exit = "exit"

        /**
         * action add this music to favorite
         */
        const val action_like = "like"

        /**
         * action remove this music from favorite
         */
        const val action_dislike = "dislike"


        /** flag that [QuietPlayerService] is Running */
        private var isRunning: Boolean = false


        /**
         * ensure [QuietPlayerService] is Running
         */
        fun ensureServiceRunning(context: Context = AppContext) {
            if (!isRunning) {
                context.startService(Intent(context, QuietPlayerService::class.java))
            } else {
                log(LoggerLevel.DEBUG) {
                    "we do not need to start music MusicPlayer service ," +
                            "because it is already running..."
                }
            }
        }

        /**
         * this value put in [Companion] is used for test,
         * cuz we can not access field before [Service.onCreate]
         *
         * [notificationHelper] is the same , is there any clever way to do this?
         */
        var musicViewModel: MusicControllerViewModel = MusicControllerViewModel()
            @VisibleForTesting set

        var notificationHelper: MusicNotification = MusicNotification()
            @VisibleForTesting set

    }

    private val playerServiceBinder = PlayerServiceBinder()

    private val musicPlayer get() = MusicPlayerManager.musicPlayer


    override fun onCreate() {
        isRunning = true
        super.onCreate()
        lifecycleRegister.markState(Lifecycle.State.STARTED)
        musicViewModel.playerState.observe(this, Observer {
            notificationHelper.update(this)
        })
        musicViewModel.playingMusic.observe(this, Observer {
            if (it == null) {
                stopForeground(false)
            }
            notificationHelper.update(this)
        })
    }

    override fun onBind(intent: Intent?): IBinder? = playerServiceBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            action_play_previous -> {
                musicViewModel.playPrevious()
            }
            action_play_pause -> {
                musicViewModel.pauseOrPlay()
            }
            action_play_next -> {
                musicViewModel.playNext()
            }
            action_exit -> {
                stopForeground(true)
                musicViewModel.quiet()
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
    private var instanceHolder: QuietMusicPlayer? = null

    private fun bindPlayerToService() {
        instanceHolder = musicPlayer
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegister.markState(Lifecycle.State.DESTROYED)
        musicPlayer.quiet()
        isRunning = false
    }


    /**
     * service binder
     */
    inner class PlayerServiceBinder : Binder() {

        /**
         * [QuietPlayerService]
         */
        val service get() = this@QuietPlayerService

    }


}