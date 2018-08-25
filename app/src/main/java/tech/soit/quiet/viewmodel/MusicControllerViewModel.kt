package tech.soit.quiet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.MusicPlayerManager
import tech.soit.quiet.utils.testing.OpenForTesting

/**
 * view model for MusicController
 */
@OpenForTesting
class MusicControllerViewModel : ViewModel() {

    /**
     * pause if playing
     * play if not playing
     */
    fun pauseOrPlay() {
        MusicPlayerManager.musicPlayer.playPause()
    }

    val playingMusic: LiveData<Music?>
        get() = MusicPlayerManager.playingMusic

    val playerState: LiveData<Int>
        get() = MusicPlayerManager.playerState

}