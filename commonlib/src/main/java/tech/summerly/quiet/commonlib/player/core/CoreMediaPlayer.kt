package tech.summerly.quiet.commonlib.player.core

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.view.animation.LinearInterpolator
import org.jetbrains.anko.coroutines.experimental.asReference
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.persistence.preference.PreferenceProvider
import tech.summerly.quiet.commonlib.player.MusicPlayerManager
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.constraints.Setting
import tv.danmaku.ijk.media.player.IMediaPlayer
import kotlin.coroutines.experimental.suspendCoroutine
import tv.danmaku.ijk.media.player.IjkMediaPlayer as MediaPlayer

/**
 * Created by summer on 18-3-4
 */
class CoreMediaPlayer {

    companion object {

        private fun getPreferenceVolume(): Float {
            var volume = try {
                val preference = PreferenceProvider.with(Setting.SETTING_PREFERENCE_PROVIDER).getPreference()
                preference.getInt("key_volume", -1)
            } catch (e: Exception) {
                log { e.printStackTrace();e.message }
                -1
            }

            if (volume == -1) {
                log(LoggerLevel.ERROR) { "can not read app setting: key_volume" }
                volume = 100
            }
            return volume / 100F
        }

        /**
         * volume from 0 to 1
         */
        var volume: Float = getPreferenceVolume()
            set(value) {
                field = value
                MusicPlayerManager.player.mediaPlayer.internalMediaPlayer.setVolume(value, value)
            }
    }

    private val internalMediaPlayer: MediaPlayer = createMediaPlayer()

    private var state = PlayerState.Idle
        set(value) {
            field = value
            MusicPlayerManager.internalPlayerState.postValue(value)
        }

    init {
        state = PlayerState.Idle
    }

    private fun createMediaPlayer(): MediaPlayer {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setOnErrorListener { _, _, extra ->
            val message = when (extra) {
                IMediaPlayer.MEDIA_ERROR_IO -> "无法获取数据"
                IMediaPlayer.MEDIA_ERROR_MALFORMED -> "数据出错"
                IMediaPlayer.MEDIA_ERROR_UNSUPPORTED -> "不支持的格式"
                IMediaPlayer.MEDIA_ERROR_TIMED_OUT -> "超时"
                else -> "未知"
            }
            log { "player error : $message , $extra" }
            state = PlayerState.Idle
            true
        }
        mediaPlayer.setOnCompletionListener {
            state = PlayerState.Complete
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        return mediaPlayer
    }


    suspend fun play(music: IMusic) {
        stop()
        reset()
        val ref = internalMediaPlayer.asReference()
        ref().setDataSource(music)
        state = PlayerState.Preparing
        ref().prepareAsyncAwait()
        //每播放一首新歌曲，都需要重置音量！！
        ref().setVolume(CoreMediaPlayer.volume, CoreMediaPlayer.volume)
        start()
    }


    fun seekTo(position: Long) {
        try {
            internalMediaPlayer.seekTo(position)
        } catch (e: IllegalStateException) {

        }
    }

    fun reset() {
        try {
            internalMediaPlayer.reset()
            state = PlayerState.Idle
        } catch (e: Exception) {
            throw e
        }
    }

    fun start() {
        try {
            internalMediaPlayer.start()
            state = PlayerState.Playing
        } catch (e: Exception) {

        }
    }

    fun pause() {
        try {
            internalMediaPlayer.pause()
            state = PlayerState.Pausing
        } catch (e: Exception) {

        }

    }

    fun getPlayerState() = state

    internal fun stop() {
        try {
            internalMediaPlayer.stop()
            state = PlayerState.Idle
        } catch (e: Exception) {
            throw e
        }
    }

    val position get() = internalMediaPlayer.currentPosition

    val duration get() = internalMediaPlayer.duration

    private suspend fun MediaPlayer.prepareAsyncAwait(): Unit = suspendCoroutine { cont ->
        setOnPreparedListener {
            cont.resume(Unit)
        }
        prepareAsync()
    }


    /**
     * 产生一个声音渐变效果,可以在暂停或者播放的时候调用
     * @param from 渐变起始的声音大小,取值范围: 0-1,0表示无声,1表示声音最大
     * @param to 渐变结束的声音大小,取值范围: 0-1,0表示无声,1表示声音最大
     * @param doneCallback 声音渐变正常结束时的回调
     */
    private fun volumeGradient(from: Float, to: Float, doneCallback: (() -> Unit)? = null) {
        val animator = ValueAnimator.ofFloat(from, to)
        animator.duration = 500
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            val volume = it.animatedValue as Float
            internalMediaPlayer.setVolume(volume, volume)
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            //TODO
        })
        animator.start()
    }

    private fun requestFocus(context: Context) {
        //请求焦点
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener {
                        if (it == AudioManager.AUDIOFOCUS_LOSS) {
                            pause()
                        }
                    }.build())
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus({
                if (it == AudioManager.AUDIOFOCUS_LOSS) {
                    pause()
                }
            }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }

    }

}