package tech.summerly.quiet.player

import tech.summerly.quiet.commonlib.base.BaseModule
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.player.service.MusicPlayerService

object PlayerModule : BaseModule() {


    override fun onCreate() {
        MusicPlayerManager.playerState.observeForever {
            if (it != null || it != PlayerState.Idle){
                onMusicPlaying()
            }
        }
    }

    private fun onMusicPlaying() {
        MusicPlayerService.start()
    }

}