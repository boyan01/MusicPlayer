package tech.summerly.quiet.commonlib.player

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import tech.summerly.quiet.commonlib.player.state.PlayerState
import tech.summerly.quiet.commonlib.utils.log

/**
 * Created by summer on 17-12-17
 *
 */
class MusicPlayerManager(context: Context) {

    companion object {

        @Suppress("ObjectPropertyName")
        @SuppressLint("StaticFieldLeak")
        private var _instance: MusicPlayerManager? = null

        val INSTANCE: MusicPlayerManager
            get() = _instance!!

        fun init(context: Context) {
            _instance = MusicPlayerManager(context)
        }
    }

    private val baseContext = context.applicationContext

    private var musicPlayer: BaseMusicPlayer? = null

    private var isBindToService = true

    fun getMusicPlayer(): BaseMusicPlayer {
        return musicPlayer ?: getOrCreateSimplePlayer()
    }

    fun setMusicPlayer(player: BaseMusicPlayer) {
        if (musicPlayer?.javaClass == player.javaClass) {
            return
        }
        musicPlayer = player
        if (player.playerState.value == PlayerState.Playing && !isBindToService) {
            bindPlayerToService()
        }
    }

    private fun bindPlayerToService() {
        log { "attempt to bind to play service" }
        baseContext.startService(Intent(baseContext, MusicPlayerService::class.java))
        isBindToService = true
    }

    fun getOrCreateSimplePlayer(): SimpleMusicPlayer {
        return musicPlayer as? SimpleMusicPlayer
                ?: SimpleMusicPlayer(baseContext).also { setMusicPlayer(it) }
    }

}

val musicPlayer: BaseMusicPlayer
    get() = MusicPlayerManager.INSTANCE.getMusicPlayer()

val simpleMusicPlayer: SimpleMusicPlayer
    get() = MusicPlayerManager.INSTANCE.getOrCreateSimplePlayer()