package tech.summerly.quiet.commonlib.player

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.state.BasePlayerDataListener
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.utils.WithDefaultLiveData
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.observeForeverFilterNull

object MusicPlayerManager : BasePlayerDataListener {


    private val internalPlayingMusic = MutableLiveData<Music>()
    private val internalMusicChange = MutableLiveData<Pair<Music?, Music?>>()
    private val internalPosition = MutableLiveData<Long>()
    private val internalPlayerState = WithDefaultLiveData(PlayerState.Idle)
    private val internalPlayMode = WithDefaultLiveData(PlayMode.Sequence)
    private val internalPlaylist = MutableLiveData<List<Music>>()


    private val onPlayerStateChange = { state: PlayerState ->
        internalPlayerState.postValue(state)
    }

    private val onPositionChange = { position: Long ->
        log { "on position change: $position" }
        internalPosition.postValue(position)
    }

    private val errorLogger = { throwable: Throwable ->
        throwable.printStackTrace()
    }

    private var musicPlayer: BaseMusicPlayer = newMusicPlayer()

    fun musicPlayer(type: MusicType? = null): BaseMusicPlayer {
        val musicPlayer = musicPlayer
        type?.let { musicPlayer.setType(it) }
        return musicPlayer
    }

    /**
     * create a new [BaseMusicPlayer] when [musicPlayer] is not available
     */
    private fun newMusicPlayer(): BaseMusicPlayer {
        return BaseMusicPlayer(
                onPlayerStateChange, MusicPlayerManager, onPositionChange, errorLogger
        )
    }

    init {

        playerState.observeForeverFilterNull {
            if (it == PlayerState.Complete) {
                musicPlayer().playNext()
            }
        }
    }


    val musicChange: LiveData<Pair<Music?, Music?>> = internalMusicChange
    val playingMusic: LiveData<Music> get() = internalPlayingMusic
    val position: LiveData<Long> get() = internalPosition
    val playerState: LiveData<PlayerState> get() = internalPlayerState
    val playMode: LiveData<PlayMode> get() = internalPlayMode
    val playlist: LiveData<List<Music>> get() = internalPlaylist

    override fun onCurrentMusicUpdated(old: Music?, new: Music?) {
        internalPlayingMusic.postValue(new)
        internalMusicChange.postValue(old to new)
        log { "change: $old to $new" }
    }

    override fun onPlayModeUpdated(playMode: PlayMode) {
        internalPlayMode.postValue(playMode)
    }

    override fun onPlaylistUpdated(playlist: List<Music>) {
        internalPlaylist.postValue(playlist)
    }

}

val musicPlayer: BaseMusicPlayer
    get() = MusicPlayerManager.musicPlayer()

