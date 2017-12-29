package tech.summerly.quiet.commonlib.player.core

import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer.*
import android.os.Build
import android.view.animation.LinearInterpolator
import kotlinx.coroutines.experimental.*
import org.jetbrains.anko.coroutines.experimental.asReference
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.state.PlayerState
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.observeForeverFilterNull
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.File
import java.net.URI
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * author : summerly
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/7/26
 * desc   :
 */
class CoreMediaPlayer {

    companion object {

        private val DURATION_UPDATE_PROGRESS = 200L

        var volume = MutableLiveData<Float>()
    }


    private var progressPublishJob: Job? = null

    private val position = MutableLiveData<Long>()

    private val playerState = MutableLiveData<PlayerState>()


    fun getPlayerState(): LiveData<PlayerState> = playerState

    private fun sendProgress() {
        progressPublishJob?.cancel()
        progressPublishJob = launch {
            val musicPlaying = currentPlaying
            while (mediaPlayer.isPlaying && musicPlaying == currentPlaying) {
                delay(DURATION_UPDATE_PROGRESS, TimeUnit.MILLISECONDS)
                position.postValue(mediaPlayer.currentPosition)
            }
        }
    }

    fun getPosition() = position


    private var mediaPlayer = IjkMediaPlayer()

    var currentPlaying: Music? = null
        private set(value) {
            field = value
        }


    init {
        //设置音量 fixme
//        val volume = AppContext.instance.defaultSharedPreferences.get(string(R.string.pref_key_audio_volume), DEFAULT_VOLUME_MAX)
//        this.volume = volume / DEFAULT_VOLUME_MAX.toFloat()

        //为mediaPlayer设置监听
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
            playerState.postValue(PlayerState.Pausing)
        }

        CoreMediaPlayer.volume.observeForeverFilterNull {
            mediaPlayer.setVolume(it, it)
        }
    }


    fun play(music: Music) {
        currentPlaying = music
        mediaPlayer.reset()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        val ref = mediaPlayer.asReference()
        async(CommonPool) {
            playerState.postValue(PlayerState.Loading)
            val url: String = music.getPlayableUrl()
            log { "准备播放 : $url" }
            ref().dataSource = url
            ref().prepareAsyncAwait()
            ref().start()
            playerState.postValue(PlayerState.Playing)
            sendProgress()
        }
    }



    private suspend fun IjkMediaPlayer.prepareAsyncAwait(): Unit = suspendCoroutine { cont ->
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
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun pause() {
        mediaPlayer.pause()
    }


    fun seekTo(position: Long) {
        log { "player: seek to $position" }
        mediaPlayer.seekTo(position)
    }

    fun release() {
        stop()
        currentPlaying = null
        mediaPlayer.release()
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
            CoreMediaPlayer.volume.value = volume
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