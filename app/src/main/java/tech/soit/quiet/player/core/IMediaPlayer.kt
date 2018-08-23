package tech.soit.quiet.player.core

import androidx.annotation.IntDef
import androidx.lifecycle.LiveData


/**
 * player interface
 */
interface IMediaPlayer {

    companion object {

        const val IDLE = 0x0

        const val PLAYING = 0x1

        const val PAUSING = 0x2

        const val PREPARING = 0x3


        @IntDef(IDLE, PLAYING, PAUSING, PREPARING)
        @Target(AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
        annotation class PlayerState

    }

    /**
     * start to play uri
     *
     * if source is not ready, this call will posted until source available
     */
    fun prepare(uri: String, playWhenReady: Boolean)

    /**
     * seek play position to [position]
     *
     * @param position millisecond
     */
    fun seekTo(position: Long)

    /**
     * flag to change the state of player
     *
     *  if set to false
     * [PLAYING] -> [PAUSING]
     * [PREPARING] -> do not play when source ready
     *
     */
    var isPlayWhenReady: Boolean

    /**
     * release all source of the player
     *
     * stop play and interrupt all jobs
     */
    fun release()

    /**
     * observable PlayerState
     *
     * @see PlayerState
     */
    fun getState(): LiveData<Int>


    /**
     * get current playing position
     *
     * if filed is not available , return 0
     */
    fun getPosition(): Long

    /**
     * get current playing music duration
     *
     * if filed is not available , return 0
     *
     */
    fun getDuration(): Long
}