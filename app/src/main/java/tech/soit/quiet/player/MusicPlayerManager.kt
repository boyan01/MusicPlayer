package tech.soit.quiet.player

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.player.playlist.Playlist
import tech.soit.quiet.ui.service.QuietPlayerService
import tech.soit.quiet.utils.component.persistence.KeyValue
import tech.soit.quiet.utils.component.persistence.get
import tech.soit.quiet.utils.component.support.liveDataWith

/**
 *
 * use [MusicPlayerManager]
 *
 * provider [musicPlayer] to access [IMediaPlayer] , [Playlist]
 * and play action such as :
 * [QuietMusicPlayer.playNext],
 * [QuietMusicPlayer.playPrevious],
 * [QuietMusicPlayer.playPause]
 *
 * provider LiveData such as [playingMusic] [position] [playerState] [playlist]
 * to listen MusicPlayer' state
 *
 * provider [play] method for convenience to play music
 *
 */
interface IMusicPlayerManager {


    var musicPlayer: QuietMusicPlayer

    val playingMusic: MutableLiveData<Music?>

    val position: MutableLiveData<Position>


    val playerState: MutableLiveData<Int>

    val playlist: MutableLiveData<Playlist>


    fun play(token: String, music: Music, list: List<Music>)


    /**
     * unit is Millisecond
     *
     * @param current the current playing position
     * @param total music total length
     */
    data class Position(val current: Long, val total: Long)

}


class MusicPlayerManagerImpl : IMusicPlayerManager {

    companion object {


        /**
         * keys use to save PlaylistData to Db
         *
         * [KEY_PLAYLIST_CURRENT] : current playing music
         * [KEY_PLAYLIST_MUSIC_LIST] : current playing music list
         * [KEY_PLAYLIST_TOKEN] : token to identify this music list
         * [KEY_PLAYLIST_PLAY_MODE] : [PlayMode]
         *
         */
        private const val KEY_PLAYLIST_MUSIC_LIST = "player_playlist_key_music_list"
        private const val KEY_PLAYLIST_TOKEN = "player_playlist_key_token"
        private const val KEY_PLAYLIST_CURRENT = "player_playlist_key_current"
        private const val KEY_PLAYLIST_PLAY_MODE = "play_playlist_key_play_mode"

    }

    /**
     * music player, manage the playlist and [IMediaPlayer]
     *
     * ATTENTION: setter is only for TEST!!
     *
     */
    override var musicPlayer = QuietMusicPlayer()

    /**
     * current playing music live data
     */
    override val playingMusic = liveDataWith(musicPlayer.playlist.current)

    override val position = MutableLiveData<IMusicPlayerManager.Position>()

    /**
     * @see IMediaPlayer.PlayerState
     */
    override val playerState = liveDataWith(IMediaPlayer.IDLE)

    init {
        musicPlayer.mediaPlayer.setOnStateChangeCallback {
            playerState.postValue(it)
        }
    }


    override val playlist = MutableLiveData<Playlist>()

    /**
     * @param token [Playlist.token]
     * @param music the music which will be play
     * @param list the music from
     */
    override fun play(token: String, music: Music, list: List<Music>) {
        val newPlaylist = Playlist(token, list)
        newPlaylist.current = music
        musicPlayer.playlist = newPlaylist
        musicPlayer.play(music)
    }

    init {
        //restore Playlist for MusicPlayer
        GlobalScope.launch Restore@{
            val token = KeyValue.get<String>(KEY_PLAYLIST_TOKEN)
            val musics: List<Music>? = KeyValue.objectFromString(KeyValue.get(KEY_PLAYLIST_MUSIC_LIST)
                    ?: return@Restore)

            if (token != null && musics != null) {
                val playMode = KeyValue.get<String>(KEY_PLAYLIST_PLAY_MODE)
                val current: Music? = KeyValue.objectFromString(KeyValue.get(KEY_PLAYLIST_CURRENT))

                val restore = Playlist(token, musics)
                restore.current = current
                restore.playMode = PlayMode.from(playMode)
                musicPlayer.playlist = restore
            }
        }

        //persistence playlist
        playlist.observeForever { pl ->
            pl ?: return@observeForever
            GlobalScope.launch {
                KeyValue.put(KEY_PLAYLIST_TOKEN, pl.token)
                KeyValue.put(KEY_PLAYLIST_CURRENT, KeyValue.objectToString(pl.current))
                KeyValue.put(KEY_PLAYLIST_PLAY_MODE, pl.playMode)
                KeyValue.put(KEY_PLAYLIST_MUSIC_LIST, KeyValue.objectToString(ArrayList(pl.list)))
            }
        }

        //persistence playing music
        playingMusic.observeForever { m ->
            m ?: return@observeForever
            GlobalScope.launch {
                KeyValue.put(KEY_PLAYLIST_CURRENT, KeyValue.objectToString(m))
            }
        }
        QuietPlayerService.init(playerState)
    }
}


object MusicPlayerManager : IMusicPlayerManager by MusicPlayerManagerImpl()
