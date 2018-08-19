package tech.soit.quiet.player

import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.player.core.QuietMediaPlayer
import tech.soit.quiet.player.playlist.Playlist
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

/**
 * provide method could directly interaction with UI
 *
 * @author 杨彬
 */
class QuietMusicPlayer {

    companion object {
        const val DURATION_UPDATE_PROGRESS = 200L
    }

    val mediaPlayer: IMediaPlayer = QuietMediaPlayer()

    var playlist: Playlist by Delegates.observable(Playlist.EMPTY) { _, _, newValue ->
        MusicPlayerManager.playlist.postValue(newValue)
    }

    fun playNext() = safeAsync {
        val next = playlist.getNext()
        if (next == null) {
            log(LoggerLevel.WARN) { "next music is null" }
            return@safeAsync
        }
        playlist.current = next
        play(next)
    }

    fun playPause() = safeAsync {
        val current = playlist.current
        if (current == null) {
            playNext()
            return@safeAsync
        }
        if (mediaPlayer.getState().value == IMediaPlayer.IDLE) {
            play(current)
        } else {
            mediaPlayer.isPlayWhenReady = !mediaPlayer.isPlayWhenReady
        }
    }

    fun playPrevious() = safeAsync {
        val previous = playlist.getPrevious()
        if (previous == null) {
            log(LoggerLevel.WARN) { "previous is null , op canceled!" }
            return@safeAsync
        }
        playlist.current = previous
        play(previous)
    }

    /**
     * play [music] , if music is not in [playlist] , insert ot next
     */
    fun play(music: Music) = safeAsync {
        if (!playlist.list.contains(music)) {
            playlist.insertToNext(music)
        }
        log { "try to play $music" }
        playlist.current = music

        val uri = music.attach[Music.URI]
        if (uri == null) {
            log(LoggerLevel.ERROR) { "next music uri is empty or null" }
            return@safeAsync
        }
        mediaPlayer.prepare(uri, true)
    }


    private fun safeAsync(block: suspend () -> Unit) {
        launch { block() }
    }

    init {

        launch {
            while (true) {
                delay(DURATION_UPDATE_PROGRESS, TimeUnit.MILLISECONDS)
                try {
                    val notify = playlist.current == null
                            && mediaPlayer.getState().value == IMediaPlayer.PLAYING

                    if (notify) {
                        MusicPlayerManager.position
                                .postValue(MusicPlayerManager.Position(mediaPlayer.getPosition(),
                                        mediaPlayer.getDuration()))
                    }
                } catch (e: Exception) {
                    //ignore
                }
            }
        }

    }

}