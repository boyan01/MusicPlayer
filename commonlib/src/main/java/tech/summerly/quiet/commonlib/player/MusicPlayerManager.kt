package tech.summerly.quiet.commonlib.player

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.playlist.Playlist
import tech.summerly.quiet.commonlib.utils.WithDefaultLiveData
import tech.summerly.quiet.commonlib.utils.asyncUI
import tech.summerly.quiet.commonlib.utils.observeFilterNull

object MusicPlayerManager {


    internal val internalPlayingMusic = MutableLiveData<Music>()
    internal val internalMusicChange = MutableLiveData<Pair<Music?, Music?>>()
    internal val internalPosition = MutableLiveData<Pair<Long, Long>>()
    internal val internalPlayerState = WithDefaultLiveData(PlayerState.Idle)
    internal val internalPlayMode = WithDefaultLiveData(PlayMode.Sequence)
    internal val internalPlaylist = MutableLiveData<List<Music>>()


    val musicChange: LiveData<Pair<Music?, Music?>> = internalMusicChange
    val playingMusic: LiveData<Music>
        get() = internalPlayingMusic
    val position: LiveData<Pair<Long, Long>> get() = internalPosition
    val playerState: LiveData<PlayerState> get() = internalPlayerState
    val playMode: LiveData<PlayMode> get() = internalPlayMode
    val playlist: LiveData<List<Music>> get() = internalPlaylist


    private var internalPlayer: MusicPlayer = MusicPlayer()

    val player: MusicPlayer get() = internalPlayer

    fun play(music: IMusic) {
        if (player.playlist.type == PlayerType.FM) {
            player.playlist = Playlist.empty()
        }
        player.play(music)
    }


    @Deprecated("", ReplaceWith("MusicPlayerManager.play(\"\", musics, music)", "tech.summerly.quiet.commonlib.player.MusicPlayerManager"))
    fun play(musics: List<IMusic>, music: IMusic? = null) {
        play("", musics, music)
    }

    fun play(token: String, list: List<IMusic>, music: IMusic? = null) {
        if (player.playlist.token != token) {
            player.playlist = Playlist.normalPlaylist(list, token)
        } else {
            player.playlist.resetMusicList(list)
        }
        if (list.isEmpty()) {
            throw IllegalArgumentException("music list can not be empty!")
        }
        if (music != null) {
            player.play(music)
        } else {
            player.playNext()
        }
    }

    init {
        playerState.observeForever(AutoPlayNext)
    }

}

private object AutoPlayNext : Observer<PlayerState> {

    override fun onChanged(t: PlayerState?) {
        if (t == PlayerState.Complete) {
            asyncUI {
                val next = MusicPlayerManager.player.playlist.getNextMusic()
                if (next != null) {
                    MusicPlayerManager.player.playNext()
                }
            }
        }
    }

}


fun LifecycleOwner.listenMusicChangePosition(items: () -> List<*>,
                                             predicate: (any: Any?, music: Music?) -> Boolean = { any, music -> any == music },
                                             change: (from: Int, to: Int) -> Unit) =
        MusicPlayerManager.musicChange.observeFilterNull(this) { (old, new) ->
            val from = items().indexOfFirst { predicate(it, old) }
            val to = items().indexOfFirst { predicate(it, new) }
            change(from, to)
        }