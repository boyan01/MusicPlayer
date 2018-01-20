package tech.summerly.quiet.commonlib.player.core

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.*
import android.os.Build
import android.view.animation.LinearInterpolator
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.CoroutineExceptionHandler
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.asReference
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.player.MusicUrlFetcher
import tech.summerly.quiet.commonlib.utils.log
import kotlin.coroutines.experimental.suspendCoroutine
import kotlin.properties.Delegates

/**
 * author : summerly
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/7/26
 * desc   :
 */
@Suppress("MemberVisibilityCanPrivate")
class CoreMediaPlayer {

    companion object {

        var volume by Delegates.observable(100F) { property, oldValue, newValue ->

        }
    }

    val position: Long get() = mediaPlayer.currentPosition.toLong()

    private val onMediaPlayerStateChangeListenerList = ArrayList<CorePlayerStateListener>()

    private var playerState by Delegates.observable(PlayerState.Idle) { _, _, newValue ->
        onMediaPlayerStateChangeListenerList.forEach {
            it(newValue)
        }
    }

    internal fun addOnMediaPlayerStateChangeListener(onPlayerStateChangeListener: CorePlayerStateListener) {
        onMediaPlayerStateChangeListenerList.add(onPlayerStateChangeListener)
    }

    fun getState() = playerState

    private val mediaPlayer = createMediaPlayer()

    var playing: Music? = null
        private set(value) {
            field = value
        }

    val isPlaying get() = mediaPlayer.isPlaying

    private fun createMediaPlayer(): MediaPlayer {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setOnErrorListener { _, _, extra ->
            val message = when (extra) {
                MEDIA_ERROR_IO -> "无法获取数据"
                MEDIA_ERROR_MALFORMED -> "数据出错"
                MEDIA_ERROR_UNSUPPORTED -> "不支持的格式"
                MEDIA_ERROR_TIMED_OUT -> "超时"
                else -> "未知"
            }
            log { "player error : $message" }
            true
        }
        mediaPlayer.setOnCompletionListener {
            //on complete todo
        }
        mediaPlayer.setVolume(CoreMediaPlayer.volume, CoreMediaPlayer.volume)
        return mediaPlayer
    }


    fun play(music: Music) = async(CommonPool + playExceptionHandler) {
        playing = music
        mediaPlayer.reset()
        @Suppress("DEPRECATION")
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        val ref = mediaPlayer.asReference()
        playerState = PlayerState.Loading
        val url = MusicUrlFetcher.getPlayableUrl(music) ?: error("can not get url")
        log { "准备播放 : $url" }
        ref().setDataSource(url)
        ref().prepareAsyncAwait()
        start()
    }

    private val playExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        playerState = PlayerState.Idle
        log { "player : error ${throwable.message}" }
    }

    private suspend fun MediaPlayer.prepareAsyncAwait(): Unit = suspendCoroutine { cont ->
        setOnPreparedListener {
            cont.resume(Unit)
        }
        prepareAsync()
    }


    /**
     * 开始或者恢复播放
     */
    fun start() {
        mediaPlayer.start()
        playerState = PlayerState.Playing
    }

    fun stop() {
        mediaPlayer.stop()
        playerState = PlayerState.Pausing
    }

    fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.Pausing
    }


    fun seekTo(position: Long) {
        mediaPlayer.seekTo(position.toInt())
    }

    fun release() {
        stop()
        playing = null
        mediaPlayer.release()
        playerState = PlayerState.Idle
        onMediaPlayerStateChangeListenerList.clear()
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
            mediaPlayer.setVolume(volume, volume)
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

    val duration: Long get() = mediaPlayer.duration.toLong()

}

typealias CorePlayerStateListener = (PlayerState) -> Unit

enum class PlayerState {
    Idle,
    Pausing,
    Playing,
    Loading,
    Complete
}