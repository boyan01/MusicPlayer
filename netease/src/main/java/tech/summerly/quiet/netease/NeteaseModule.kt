package tech.summerly.quiet.netease

import android.content.Context
import tech.summerly.quiet.commonlib.base.BaseModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicPlaylistProvider
import tech.summerly.quiet.commonlib.player.MusicPlaylistProviderFactory
import tech.summerly.quiet.commonlib.player.MusicUrlFetcher
import tech.summerly.quiet.commonlib.player.state.BasePlayerDataListener
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.netease.NeteaseModule.onCreate
import tech.summerly.quiet.netease.player.NeteaseFmPlaylistProvider
import tech.summerly.quiet.netease.utils.NeteaseMusicUrlGetter

/**
 * module global context holder
 * [onCreate] will be invoke after application created
 */
internal object NeteaseModule : BaseModule() {


    override fun onCreate() {
        MusicUrlFetcher.addMusicUrlGetter(MusicType.NETEASE, NeteaseMusicUrlGetter)
        MusicUrlFetcher.addMusicUrlGetter(MusicType.NETEASE_FM, NeteaseMusicUrlGetter)
        MusicPlaylistProviderFactory.setFactory(MusicType.NETEASE_FM, object : MusicPlaylistProviderFactory() {
            override fun createMusicPlaylistProvider(
                    current: Music?, playMode: PlayMode,
                    musicList: ArrayList<Music>,
                    playerStateListener: BasePlayerDataListener
            ): MusicPlaylistProvider {
                return NeteaseFmPlaylistProvider(current, musicList, playerStateListener)
            }
        })
    }
}