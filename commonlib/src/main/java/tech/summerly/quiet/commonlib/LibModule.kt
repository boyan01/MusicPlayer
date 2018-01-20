package tech.summerly.quiet.commonlib

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.stetho.Stetho
import tech.summerly.quiet.commonlib.base.BaseModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicPlaylistProvider
import tech.summerly.quiet.commonlib.player.MusicPlaylistProviderFactory
import tech.summerly.quiet.commonlib.player.SimplePlaylistProvider
import tech.summerly.quiet.commonlib.player.state.BasePlayerDataListener
import tech.summerly.quiet.commonlib.player.state.PlayMode

/**
 * Created by summer on 17-12-17.
 * Base Application context
 */
internal object LibModule : BaseModule() {


    override fun onCreate() {

        Stetho.initializeWithDefaults(this)
        if (BuildConfig.DEBUG) {
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(applicationContext as Application?)
        val simplePlaylistFactory = object : MusicPlaylistProviderFactory() {
            override fun createMusicPlaylistProvider(current: Music?, playMode: PlayMode, musicList: ArrayList<Music>, playerStateListener: BasePlayerDataListener): MusicPlaylistProvider {
                return SimplePlaylistProvider(current, playMode, musicList, playerStateListener)
            }
        }
        MusicPlaylistProviderFactory.setFactory(MusicType.LOCAL, simplePlaylistFactory)
        MusicPlaylistProviderFactory.setFactory(MusicType.NETEASE, simplePlaylistFactory)

    }

}