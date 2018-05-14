package tech.summerly.quiet.commonlib.player

import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.core.CoreMediaPlayer
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.playlist.Playlist
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import java.util.concurrent.TimeUnit

class MusicPlayer {

    companion object {
        const val DURATION_UPDATE_PROGRESS = 200L

    }

    var playlist: Playlist = Playlist.empty()
        set(value) {
            field.inActive()
            field = value
            value.active()
            resetMediaPlayer()
            log { "set playlist $value" }
        }

    private fun resetMediaPlayer() {
        mediaPlayer.reset()
    }

    val mediaPlayer: CoreMediaPlayer = CoreMediaPlayer()

    var playMode: PlayMode = PlayMode.Sequence
        set(value) {
            field = value
            MusicPlayerManager.internalPlayMode.postValue(value)
            async {
                PlayerPersistenceHelper.savePlayMode(value)
            }
        }

    init {
        //再次赋值是为了发送事件
        playMode = PlayerPersistenceHelper.restorePlayMode()
        playlist = PlayerPersistenceHelper.restorePlaylist() ?: Playlist.empty()
    }

    fun playNext() = safeAsync {
        val next = playlist.getNextMusic()
        if (next == null) {
            log(LoggerLevel.WARN) { "next music is null" }
            return@safeAsync
        }
        playlist.current = next
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
        playlist.current = previous
        mediaPlayer.play(previous)
    }

    fun destroy() {
        //TODO
    }

    /**
     * @param music Must be in [playlist]
     */
    fun play(music: IMusic) = safeAsync {
        music as Music
        if (!playlist.musics.contains(music)) {
            playlist.insertToNext(music)
        }
        log { "try to play $music" }
        playlist.current = music
        mediaPlayer.play(music)
    }


    private fun safeAsync(block: suspend () -> Unit) {
        async { block() }
    }


    init {

        launch {
            while (true) {
                delay(DURATION_UPDATE_PROGRESS, TimeUnit.MILLISECONDS)
                try {
                    if (playlist.current == null) {
                        continue
                    }
                    if (mediaPlayer.getPlayerState() == PlayerState.Playing || mediaPlayer.getPlayerState() == PlayerState.Pausing) {
                        MusicPlayerManager.internalPosition.postValue(mediaPlayer.position to mediaPlayer.duration)
                    } else {
                        MusicPlayerManager.internalPosition.postValue(0L
                                to (playlist.current?.duration ?: 0))
                    }
                } catch (e: Exception) {
                    //
                }
            }
        }

    }

}