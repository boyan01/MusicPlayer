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
     *
     * @see tech.soit.quiet.player.QuietMusicPlayer.playPause
     */
    fun pauseOrPlay() {
        MusicPlayerManager.musicPlayer.playPause()
    }

    /**
     * @see tech.soit.quiet.player.QuietMusicPlayer.playPrevious
     */
    fun playPrevious() {
        MusicPlayerManager.musicPlayer.playPrevious()
    }

    /**
     * @see tech.soit.quiet.player.QuietMusicPlayer.playNext
     */
    fun playNext() {
        MusicPlayerManager.musicPlayer.playNext()
    }

    /**
     * @see tech.soit.quiet.player.QuietMusicPlayer.quiet
     */
    fun quiet() {
        MusicPlayerManager.musicPlayer.quiet()
    }

    /**
     * @see MusicPlayerManager.playingMusic
     */
    val playingMusic: LiveData<Music?>
        get() = MusicPlayerManager.playingMusic


    /**
     * @see MusicPlayerManager.playerState
     */
    val playerState: LiveData<Int>
        get() = MusicPlayerManager.playerState

}