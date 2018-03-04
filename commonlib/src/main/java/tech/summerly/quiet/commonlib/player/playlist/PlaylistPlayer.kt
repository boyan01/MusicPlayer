package tech.summerly.quiet.commonlib.player.playlist

import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.core.CoreMediaPlayer
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.persistence.PlaylistStorage
import java.util.concurrent.TimeUnit

/**
 * Created by summer on 18-3-4
 */
class PlaylistPlayer internal constructor(
        stateChange: (PlayerState) -> Unit,
        private val positionChange: (current: Long, total: Long) -> Unit
) {

    companion object {
        private const val DURATION_UPDATE_PROGRESS = 200L
    }

    var playlist: Playlist = PlaylistStorage.resotrePlaylist()

    @Deprecated("", ReplaceWith("playlist"))
    val playlistProvider
        get() = playlist

    /**
     * 更换播放列表到指定的类型
     */
    fun switchPlaylist(type: Int = Playlist.TYPE_DEFAULT) {
        if (playlist.type == type) {
            return
        }
        playlist = Playlist.newInstance(type)
        PlaylistStorage.saveType(type)
    }

    private val mediaPlayer = CoreMediaPlayer(stateChange)

    val current get() = playlist.current

    /**
     * everything get ready , just to start play music
     */
    private suspend fun performPlay(music: Music) {
        //check playlist provider whether accept this music type
        if (!playlist.musicList.contains(music)) {
            playlist.insertToNext(music)
        }
        playlist.current = music
        mediaPlayer.play(music)
        startSendProgressTask()
    }

    fun playNext() = async {
        val next = playlist.getNextMusic() ?: return@async
        performPlay(next)
    }

    fun playPrevious() = async {
        val previous = playlist.getPreviousMusic() ?: return@async
        performPlay(previous)
    }

    fun playPause() = async {
        when (mediaPlayer.getPlayerState()) {
            PlayerState.Pausing -> {
                mediaPlayer.start()
                startSendProgressTask()
            }
            PlayerState.Playing -> {
                mediaPlayer.pause()
            }
            PlayerState.Preparing -> Unit
            else -> {
                val shouldBePlay = playlist.current ?: playlist.getNextMusic()
                shouldBePlay?.let {
                    performPlay(it)
                }
            }
        }
    }

    fun play(music: Music) = async {
        performPlay(music)
    }

    fun seekTo(position: Long) {
        mediaPlayer.seekTo(position)
        positionChange(mediaPlayer.position, mediaPlayer.duration)
    }

    private var progressPublishJob: Job? = null

    private fun startSendProgressTask() {
        progressPublishJob?.cancel()
        progressPublishJob = launch {
            val musicPlaying = current
            while (mediaPlayer.getPlayerState() == PlayerState.Playing && musicPlaying == current) {
                delay(DURATION_UPDATE_PROGRESS, TimeUnit.MILLISECONDS)
                positionChange(mediaPlayer.position, mediaPlayer.duration)
            }
        }
    }

    fun exit() {
        //TODO
        mediaPlayer.stop()

    }

    fun getState(): PlayerState = mediaPlayer.getPlayerState()

    val duration = mediaPlayer.duration

}