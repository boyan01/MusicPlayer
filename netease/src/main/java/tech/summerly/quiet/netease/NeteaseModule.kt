package tech.summerly.quiet.netease

import tech.summerly.quiet.commonlib.base.BaseModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicPlaylistProvider
import tech.summerly.quiet.commonlib.player.MusicPlaylistProviderFactory
import tech.summerly.quiet.commonlib.player.state.BasePlayerDataListener
import tech.summerly.quiet.commonlib.player.state.PlayMode
import tech.summerly.quiet.netease.NeteaseModule.onCreate
import tech.summerly.quiet.netease.player.NeteaseFmPlaylistProvider

/**
 * module global context holder
 * [onCreate] will be invoke after application created
 */
internal object NeteaseModule : BaseModule() {


    override fun onCreate() {
        MusicPlaylistProviderFactory.setFactory(MusicType.NETEASE_FM, object : MusicPlaylistProviderFactory() {
            override fun createMusicPlaylistProvider(
                    current: Music?, playMode: PlayMode,
                    musicList: ArrayList<Music>,
                    playerStateListener: BasePlayerDataListener
            ): MusicPlaylistProvider {
                musicList.removeAll { it.type != MusicType.NETEASE_FM }
                return NeteaseFmPlaylistProvider(current.takeIf { it?.type == MusicType.NETEASE_FM },
                        musicList,
                        playerStateListener)
            }
        })
    }
}