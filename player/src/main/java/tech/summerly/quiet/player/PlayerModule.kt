package tech.summerly.quiet.player

import android.app.Application
import tech.summerly.quiet.commonlib.base.BaseModule
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.player.service.MusicPlayerService
import tech.summerly.quiet.player.utils.BottomControllerInitial

object PlayerModule : BaseModule() {


    override fun onCreate() {
        MusicPlayerManager.playerState.observeForever {
            if (it == PlayerState.Playing) {
                onMusicPlaying()
            }
        }
        (baseContext as Application).registerActivityLifecycleCallbacks(BottomControllerInitial)
    }

    private fun onMusicPlaying() {
        MusicPlayerService.start()
    }

}