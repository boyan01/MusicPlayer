package tech.summerly.quiet.commonlib.player

import android.content.Intent
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.core.CoreMediaPlayer
import tech.summerly.quiet.commonlib.player.core.CorePlayerStateListener
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.service.MusicPlayerService
import tech.summerly.quiet.commonlib.player.state.BasePlayerDataListener
import tech.summerly.quiet.commonlib.player.state.BasePlayerDataSaver
import tech.summerly.quiet.commonlib.player.state.plus
import tech.summerly.quiet.commonlib.utils.log
import java.util.concurrent.TimeUnit

typealias OnPositionChange = (position: Long) -> Unit
typealias OnError = (Throwable) -> Unit

class BaseMusicPlayer(
        private val onPlayerStateChange: CorePlayerStateListener,
        private val basePlayerDataListener: BasePlayerDataListener,
        private val onPositionChange: OnPositionChange,
        private val onError: OnError
) {

    companion object {
        private val DURATION_UPDATE_PROGRESS = 200L
    }

    /**
     * initial as simple music player playlist provider
     */
    private var internalPlaylistProvider = newPlaylistProvider(null)

    val corePlayer: CoreMediaPlayer = CoreMediaPlayer()
            .also { it.addOnMediaPlayerStateChangeListener(onPlayerStateChange) }

    val current: Music? get() = playlistProvider.current

    val playlistProvider get() = internalPlaylistProvider

    /**
     * everything get ready , just to start play music
     */
    private fun performPlay(music: Music) {
        //check playlist provider whether accept this music type
        setType(music.type)
        if (!playlistProvider.getPlaylist().contains(music)) {
            playlistProvider.insertToNext(music)
        }
        if (corePlayer.playing != music) {
            playlistProvider.current = music
        }
        launch {
            corePlayer.play(music).await()
            sendProgress()
        }
        bindPlayerToService()
    }

    private fun bindPlayerToService() {
        log { "attempt to bind to play service" }
        LibModule.startService(Intent(LibModule, MusicPlayerService::class.java))
    }

    fun playNext() = launch {
        val next = playlistProvider.getNextMusic() ?: return@launch
        performPlay(next)
    }

    fun playPrevious() = launch {
        val previous = playlistProvider.getPreviousMusic() ?: return@launch
        performPlay(previous)
    }

    fun playPause() = launch {
        when (corePlayer.getState()) {
            PlayerState.Pausing -> {
                corePlayer.start()
                sendProgress()
            }
            PlayerState.Playing -> corePlayer.pause()
            PlayerState.Loading -> Unit
            else -> {
                val shouldBePlay = playlistProvider.current ?: playlistProvider.getNextMusic()
                shouldBePlay?.let(this@BaseMusicPlayer::performPlay)
                Unit
            }
        }
    }

    /**
     * try to play [music]
     * if this music is playing , do nothing
     * else will force to play this music from start , even it is pausing
     */
    fun play(music: Music) {
        log { music.toShortString() }
        performPlay(music)
    }

    internal fun destroy() {

    }

    private fun newPlaylistProvider(type: MusicType?): MusicPlaylistProvider {
        return MusicPlaylistProviderFactory[type, basePlayerDataListener + BasePlayerDataSaver]
    }

    fun setType(type: MusicType) {
        if (!internalPlaylistProvider.isTypeAccept(type)) { // if need to change provider type
            corePlayer.stop()
            internalPlaylistProvider = newPlaylistProvider(type)
            playNext()
        }
    }

    private var progressPublishJob: Job? = null

    private fun sendProgress() {
        progressPublishJob?.cancel()
        progressPublishJob = launch {
            val musicPlaying = current
            while (corePlayer.isPlaying && musicPlaying == current) {
                delay(DURATION_UPDATE_PROGRESS, TimeUnit.MILLISECONDS)
                onPositionChange(corePlayer.position)
            }
        }
    }

    fun seekTo(position: Long) {
        corePlayer.seekTo(position)
        onPositionChange(position)
    }
}
