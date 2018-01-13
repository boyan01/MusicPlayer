package tech.summerly.quiet.netease

import android.annotation.SuppressLint
import android.content.Context
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicUrlManager
import tech.summerly.quiet.netease.utils.NeteaseMusicUrlGetter

/**
 * module global context holder
 * [onCreate] will be invoke after application created
 */
class NeteaseModule {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        val instance get() = context!!
    }

    fun onCreate(context: Context) {
        NeteaseModule.context = context.applicationContext
        MusicUrlManager.addMusicUrlGetter(MusicType.NETEASE, NeteaseMusicUrlGetter)
        MusicUrlManager.addMusicUrlGetter(MusicType.NETEASE_FM, NeteaseMusicUrlGetter)
    }
}