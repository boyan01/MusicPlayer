package tech.summerly.quiet.netease.api

import android.content.Context
import okhttp3.Cache
import tech.summerly.quiet.commonlib.cookie.PersistentCookieStore

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/23
 * desc   :
 */
class NeteaseCloudMusicApi(context: Context) {

    private val neteaseService = CloudMusicServiceProvider()
            .provideCloudMusicService(PersistentCookieStore(context.applicationContext),
                    Cache(context.cacheDir, 1024 * 1024))

}