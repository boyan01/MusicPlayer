package tech.summerly.quiet.commonlib.player

import android.annotation.SuppressLint
import android.content.Context
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


    fun getMusicPlayer(): BaseMusicPlayer {
        val player = musicPlayer ?: SimpleMusicPlayer(baseContext).also { musicPlayer = it }
        bindPlayerToService(player = player)
        return player
    }

    fun setMusicPlayer(player: BaseMusicPlayer) {
        musicPlayer = player
        bindPlayerToService(player)
    }

    private fun bindPlayerToService(player: BaseMusicPlayer) {
        log { "attempt to bind to play service" }
    }

}