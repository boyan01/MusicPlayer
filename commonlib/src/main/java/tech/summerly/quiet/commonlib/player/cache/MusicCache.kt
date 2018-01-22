package tech.summerly.quiet.commonlib.player.cache

import android.content.Context
import com.danikula.videocache.HttpProxyCacheServer
import com.danikula.videocache.headers.HeaderInjector
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.utils.headerNetease
import java.io.File

/**
 *
 */
object MusicCacheHelper {

    private val context: Context get() = LibModule

    private const val DIR_MUSIC_CACHE = "music_cache"

    private val cacheRoot = File(context.externalCacheDir, DIR_MUSIC_CACHE).also { it.mkdirs() }

    // 1 Gb for default cache
    private const val SIZE_CACHE_DEFAULT = 1024 * 1024 * 1024L

    private val musicCacheServer = HttpProxyCacheServer.Builder(context)
            .cacheDirectory(cacheRoot)
            .maxCacheSize(SIZE_CACHE_DEFAULT)
            .fileNameGenerator {
                //usually netease music url end with .../../[md5].mp3
                it.substringAfterLast("/")
            }
            .headerInjector {
                if (it.startsWith("http://m10.music.126.net")) {
                    headerNetease
                } else {
                    emptyMap()
                }
            }
            .build()

    fun generateProxyUrl(uri: String): String {
        return musicCacheServer.getProxyUrl(uri, true)
    }

}