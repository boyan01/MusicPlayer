package tech.soit.quiet.player

import androidx.lifecycle.MutableLiveData
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.core.IMediaPlayer
import tech.soit.quiet.player.playlist.Playlist

object MusicPlayerManager {


    /**
     * unit is Millisecond
     *
     * @param current the current playing postiion
     * @param total music total length
     */
    data class Position(val current: Long, val total: Long)


    /**
     * music changed live data
     */
    val musicChange = MutableLiveData<Pair<Music?, Music?>>()

    /**
     * current playing music live data
     */
    val playingMusic = MutableLiveData<Music?>()

    val position = MutableLiveData<Position>()

    /**
     * @see IMediaPlayer.PlayerState
     */
    val playerState = MutableLiveData<Int>()


    val playlist = MutableLiveData<Playlist>()

    /**
     * @param token [Playlist.token]
     * @param music the music which will be play
     * @param list the music from
     */
    fun play(token: String, music: Music, list: List<Music>) {

    }

}