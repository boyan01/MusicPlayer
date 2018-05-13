package tech.summerly.quiet.commonlib.player

import kotlinx.coroutines.experimental.async
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.core.CoreMediaPlayer
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.persistence.PlaylistStorage
import tech.summerly.quiet.commonlib.player.playlist.Playlist
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log

class MusicPlayer {

    var playlist: Playlist = PlaylistStorage.restorePlaylist()

    val mediaPlayer: CoreMediaPlayer = CoreMediaPlayer { }

    fun playNext() = safeAsync {
        val next = playlist.getNextMusic()
        if (next == null) {
            log(LoggerLevel.WARN) { "next music is null" }
            return@safeAsync
        }
        mediaPlayer.play(next)
    }

    fun playPause() = safeAsync {
        when (mediaPlayer.getPlayerState()) {
            PlayerState.Playing -> {
                mediaPlayer.pause()
            }
            PlayerState.Pausing -> {
                mediaPlayer.start()
            }
            PlayerState.Preparing -> Unit
            else -> {
                val shouldBePlay = playlist.current ?: playlist.getNextMusic()
                shouldBePlay?.let {
                    mediaPlayer.play(it)
                }
            }
        }
    }

    fun playPrevious() = safeAsync {
        val previous = playlist.getPreviousMusic()
        if (previous == null) {
            log(LoggerLevel.WARN) { "previous is null , op canceled!" }
            return@safeAsync
        }
        mediaPlayer.play(previous)
    }

    fun destroy() {
        //TODO
    }


    private fun safeAsync(block: suspend () -> Unit) {
        async { block() }
    }

    fun play(music: IMusic) = safeAsync {
        mediaPlayer.play(music as Music)
    }

}