package tech.summerly.quiet.local

import android.annotation.SuppressLint
import android.content.Context
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.player.MusicUrlFetcher
import tech.summerly.quiet.local.utils.LocalMusicUrlGetter

/**
 * author : yangbin10
 * date   : 2018/1/15
 */
class LocalModule {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null

        val instance get() = context!!
    }

    fun onCreate(context: Context) {
        LocalModule.context = context.applicationContext
        MusicUrlFetcher.addMusicUrlGetter(MusicType.LOCAL, LocalMusicUrlGetter)
    }
}