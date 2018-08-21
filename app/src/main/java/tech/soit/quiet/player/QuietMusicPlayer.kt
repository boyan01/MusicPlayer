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

    /**
     * @see IMediaPlayer
     */
    val mediaPlayer: IMediaPlayer = QuietMediaPlayer()

    /**
     * @see Playlist
     */
    var playlist: Playlist by Delegates.observable(Playlist.EMPTY) { _, _, newValue ->
        MusicPlayerManager.playlist.postValue(newValue)
    }


    /**
     * play the music which return by [Playlist.getNext]
     */
    fun playNext() = safeAsync {
        val next = playlist.getNext()
        if (next == null) {
            log(LoggerLevel.WARN) { "next music is null" }
            return@safeAsync
        }
        play(next)
    }

    /**
     * if is playing , pause
     * if is not playing , playing current music
     */
    fun playPause() = safeAsync {
        val current = playlist.current
        if (current == null) {
            playNext()
            return@safeAsync
        }
        if (current != MusicPlayerManager.playingMusic.value) {
            MusicPlayerManager.playingMusic.postValue(current)
        }
        if (mediaPlayer.getState().value == IMediaPlayer.IDLE) {
            play(current)
        } else {
            mediaPlayer.isPlayWhenReady = !mediaPlayer.isPlayWhenReady
        }
    }

    /**
     * play the music which return by [Playlist.getPrevious]
     */
    fun playPrevious() = safeAsync {
        val previous = playlist.getPrevious()
        if (previous == null) {
            log(LoggerLevel.WARN) { "previous is null , op canceled!" }
            return@safeAsync
        }
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

        //live data playing music changed
        MusicPlayerManager.playingMusic.postValue(music)

        val uri = music.attach[Music.URI]
        if (uri == null) {
            log(LoggerLevel.ERROR) { "next music uri is empty or null" }
            return@safeAsync
        }
        mediaPlayer.prepare(uri, true)
    }


    /**
     * stop play
     */
    fun quiet() {
        mediaPlayer.release()
    }


    private fun safeAsync(block: suspend () -> Unit) {
        launch { block() }
    }

    init {

        //indefinite to emit current playing music' duration and playing position
        //maybe have a cleverer way to do that!!
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