package tech.summerly.quiet.commonlib.player

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.playlist.Playlist
import tech.summerly.quiet.commonlib.player.playlist.PlaylistPlayer
import tech.summerly.quiet.commonlib.player.service.MusicPlayerService
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.commonlib.utils.WithDefaultLiveData
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.observeFilterNull
import tech.summerly.quiet.commonlib.utils.observeForeverFilterNull

object MusicPlayerManager : Playlist.StateChangeListener {


    private val internalPlayingMusic = MutableLiveData<Music>()
    private val internalMusicChange = MutableLiveData<Pair<Music?, Music?>>()
    private val internalPosition = MutableLiveData<Pair<Long, Long>>()
    private val internalPlayerState = WithDefaultLiveData(PlayerState.Idle)
    private val internalPlayMode = WithDefaultLiveData(PlayMode.Sequence)
    private val internalPlaylist = MutableLiveData<List<Music>>()


    private val onPlayerStateChange = { state: PlayerState ->
        if (state != PlayerState.Idle) {
            MusicPlayerService.start()
        }
        internalPlayerState.postValue(state)
    }

    private val onPositionChange = { current: Long, total: Long ->
        internalPosition.postValue(current to total)
    }

    override fun onMusicChange(old: Music?, new: Music?) {
        internalPlayingMusic.postValue(new)
        internalMusicChange.postValue(old to new)
        log { "change: $old to $new" }
    }

    override fun onPlayModeChange(playMode: PlayMode) {
        internalPlayMode.postValue(playMode)
    }

    override fun onMusicListChange(musicList: List<Music>) {
        internalPlaylist.postValue(musicList)
    }

    private val player: PlaylistPlayer = PlaylistPlayer(
            onPlayerStateChange, onPositionChange)

    fun musicPlayer(type: MusicType? = null): PlaylistPlayer {
        if (type == null) {//不作任何处理，直接返回 player
            return player
        }
        val t = when (type) {
            MusicType.NETEASE_FM -> Playlist.TYPE_FM
            MusicType.LOCAL, MusicType.NETEASE -> Playlist.TYPE_NORMAL
        }
        player.switchPlaylist(t)
        return player
    }

    init {
        playerState.observeForeverFilterNull {
            if (it == PlayerState.Complete) {
                musicPlayer().playNext()
            }
        }
    }

    val musicChange: LiveData<Pair<Music?, Music?>> = internalMusicChange
    @Deprecated("使用 musicChange 来监听歌曲变化")
    val playingMusic: LiveData<Music>
        get() = internalPlayingMusic
    val position: LiveData<Pair<Long, Long>> get() = internalPosition
    val playerState: LiveData<PlayerState> get() = internalPlayerState
    val playMode: LiveData<PlayMode> get() = internalPlayMode
    val playlist: LiveData<List<Music>> get() = internalPlaylist

}

val musicPlayer: PlaylistPlayer
    get() = MusicPlayerManager.musicPlayer()

fun LifecycleOwner.listenMusicChangePosition(items: List<*>,
                                             predicate: (any: Any?, music: Music?) -> Boolean = { any, music -> any == music },
                                             change: (from: Int, to: Int) -> Unit) =
        MusicPlayerManager.musicChange.observeFilterNull(this) { (old, new) ->
            val from = items.indexOfFirst { predicate(it, old) }
            val to = items.indexOfFirst { predicate(it, new) }
            change(from, to)
        }

@Deprecated("这是重构移除的组件！", replaceWith = ReplaceWith("PlaylistPlayer", imports = ["tech.summerly.quiet.commonlib.player.playlist.PlaylistPlayer"]))
typealias BaseMusicPlayer = PlaylistPlayer