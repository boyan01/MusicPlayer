package tech.summerly.quiet.commonlib.player

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.core.PlayerState
import tech.summerly.quiet.commonlib.player.service.MusicPlayerService
import tech.summerly.quiet.commonlib.utils.WithDefaultLiveData
import tech.summerly.quiet.commonlib.utils.log

/**
 * Created by summer on 17-12-17
 *
 */
object MusicPlayerManager {


    val playerState: LiveData<PlayerState> get() = internalPlayerState

    private val internalPlayerState = WithDefaultLiveData<PlayerState>(PlayerState.Idle)


    private val onPlayerStateChange = { state: PlayerState ->
        internalPlayerState.postValue(state)
    }

    private val onPositionChange = { position: Long ->
        internalPosition.postValue(position)
    }

    private val onMusicChange = { _: Music?, new: Music? ->
        internalPlayingMusic.postValue(new)
    }

    private val onError = { throwable: Throwable ->

    }


    private val baseContext get() = LibModule.instance

    private var musicPlayer: BaseMusicPlayer? = null

    fun musicPlayer(type: MusicType = MusicType.LOCAL): BaseMusicPlayer {
        val musicPlayer = musicPlayer ?: synchronized(MusicPlayerManager) {
            musicPlayer ?: newMusicPlayer().also { musicPlayer = it }

        }
        musicPlayer.setType(type)
        return musicPlayer
    }

    /**
     * create a new [BaseMusicPlayer] when [musicPlayer] is not available
     */
    private fun newMusicPlayer(): BaseMusicPlayer {
        val player = BaseMusicPlayer(
                onMusicChange, onPlayerStateChange, onPositionChange, onError
        )
        bindPlayerToService()
        return player
    }

    private fun bindPlayerToService() {
        log { "attempt to bind to play service" }
        baseContext.startService(Intent(baseContext, MusicPlayerService::class.java))
    }

    val playingMusic: LiveData<Music> get() = internalPlayingMusic
    private val internalPlayingMusic = MutableLiveData<Music>()
    val position: LiveData<Long> get() = internalPosition
    private val internalPosition = MutableLiveData<Long>()

}

val musicPlayer: BaseMusicPlayer
    get() = MusicPlayerManager.musicPlayer()

