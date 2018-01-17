package tech.summerly.quiet.commonlib

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.stetho.Stetho
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicPlaylistProvider
import tech.summerly.quiet.commonlib.player.MusicPlaylistProviderFactory
import tech.summerly.quiet.commonlib.player.SimplePlaylistProvider
import tech.summerly.quiet.commonlib.player.state.PlayMode

/**
 * Created by summer on 17-12-17.
 * Base Application context
 */
class LibModule {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        val instance: Context
            get() = context!!
    }

    fun onCreate(context: Context) {
        LibModule.context = context
        Stetho.initializeWithDefaults(context)
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(context.applicationContext as Application?)
        val simplePlaylistFactory = object : MusicPlaylistProviderFactory() {
            override fun createMusicPlaylistProvider(current: Music?, playMode: PlayMode, musicList: ArrayList<Music>): MusicPlaylistProvider {
                return SimplePlaylistProvider(current, playMode, musicList)
            }
        }
        MusicPlaylistProviderFactory.setFactory(MusicType.LOCAL, simplePlaylistFactory)
        MusicPlaylistProviderFactory.setFactory(MusicType.NETEASE, simplePlaylistFactory)
    }
}