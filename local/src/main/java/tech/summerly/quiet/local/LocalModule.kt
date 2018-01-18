package tech.summerly.quiet.local

import tech.summerly.quiet.commonlib.base.BaseModule
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicUrlFetcher
import tech.summerly.quiet.local.utils.LocalMusicUrlGetter

/**
 * author : yangbin10
 * date   : 2018/1/15
 */
object LocalModule : BaseModule() {


    override fun onCreate() {
        MusicUrlFetcher.addMusicUrlGetter(MusicType.LOCAL, LocalMusicUrlGetter)
    }
}