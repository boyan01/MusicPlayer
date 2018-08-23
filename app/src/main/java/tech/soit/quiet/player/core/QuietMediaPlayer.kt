package tech.soit.quiet.player.core

import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.LiveData
import tech.soit.quiet.AppContext
import tech.soit.quiet.utils.component.support.liveDataWith
import kotlin.properties.Delegates

/**
 * [IMediaPlayer] impl by [android.media.MediaPlayer]
 */
class QuietMediaPlayer(
        private val player: MediaPlayer = MediaPlayer()
) : IMediaPlayer {

    private val _state = liveDataWith(IMediaPlayer.IDLE)

    private var state by Delegates.observable(IMediaPlayer.IDLE) { _, _, newValue ->
        //might invoke by worker thread
        _state.postValue(newValue)
    }


    private var _isPlayWhenReady: Boolean = false

    override var isPlayWhenReady: Boolean
        get() = _isPlayWhenReady
        set(value) {
            _isPlayWhenReady = value
            if (value && state == IMediaPlayer.PAUSING) {
                player.start()
                state = IMediaPlayer.PLAYING
            } else if (!value && state == IMediaPlayer.PLAYING) {
                player.pause()
                state = IMediaPlayer.PAUSING
            }
        }

    override fun prepare(uri: String, playWhenReady: Boolean) {
        if (state != IMediaPlayer.IDLE) {
            player.reset()
            state = IMediaPlayer.IDLE
        }
        isPlayWhenReady = playWhenReady
        player.setDataSource(AppContext, Uri.parse(uri))
        player.setOnPreparedListener {
            //change state when player prepared
            state = IMediaPlayer.PAUSING

            if (isPlayWhenReady) {
                it.start()
                state = IMediaPlayer.PLAYING
            }
        }
        state = IMediaPlayer.PREPARING
        player.prepareAsync()
    }


    override fun seekTo(position: Long) {
        if (state == IMediaPlayer.IDLE) {
            return
        } else if (state == IMediaPlayer.PREPARING) {
            //do nothing if is preparing
            return
        } else if (state == IMediaPlayer.PLAYING || state == IMediaPlayer.PAUSING) {
            player.seekTo(position.toInt())
        }
    }


    override fun release() {
        isPlayWhenReady = false
        state = IMediaPlayer.IDLE
        player.reset()
    }

    override fun getState(): LiveData<Int> {
        return _state
    }

    override fun getPosition(): Long {
        return player.currentPosition.toLong()
    }

    override fun getDuration(): Long {
        if (state == IMediaPlayer.IDLE || state == IMediaPlayer.PREPARING) {
            return 0L
        }
        return player.duration.toLong()
    }


}