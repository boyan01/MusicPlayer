package tech.summerly.quiet.commonlib.player.core

import android.net.Uri
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.bean.MusicUri
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.commonlib.utils.md5
import tech.summerly.quiet.commonlib.utils.retryNull
import tech.summerly.quiet.commonlib.utils.string
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi
import tech.summerly.streamcache.CachedDataSource
import tech.summerly.streamcache.DataSource
import tech.summerly.streamcache.DirectDataSource
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.misc.IMediaDataSource
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URI

/**
 * Created by summer on 18-3-5
 */

suspend fun IjkMediaPlayer.setDataSource(music: IMusic) {
    val url = MusicUrlSource.getPlayableUri(music)
    if (url == null || url.isEmpty()) {
        throw IOException(string(R.string.can_not_find_music_url))
    }
    val needCache = !url.startsWith("file", true)
    if (needCache) {
        setDataSource(MediaDataSource.with(url))
    } else {
        setDataSource(LibModule, Uri.parse(url))
    }
}

class MediaDataSource(dataSource: DataSource) : DataSource by dataSource, IMediaDataSource {

    companion object {

        fun with(url: String): MediaDataSource {
            val dataSource = CachedDataSource(Uri.parse(url))
            return MediaDataSource(dataSource)
        }

        /**
         *
         * @throws IOException
         */
        suspend operator fun invoke(music: Music): MediaDataSource {
            val url = MusicUrlSource.getPlayableUri(music)
                    ?: throw IOException(string(R.string.can_not_find_music_url))
            return invoke(url, !url.startsWith("file", true))
        }

        /**
         * @param cache cache to local file if set true
         */
        operator fun invoke(url: String, cache: Boolean = true): MediaDataSource {
            val datasource = if (cache) {
                CachedDataSource(Uri.parse(url), DefaultNameGenerator)
            } else {
                DirectDataSource(Uri.parse(url))
            }
            return MediaDataSource(datasource)
        }

    }

    private object DefaultNameGenerator : (String) -> String {
        override fun invoke(url: String): String {
            val filename = url.substringAfterLast('/')
            return if (filename.isEmpty()) {
                url.md5()
            } else {
                filename
            }
        }
    }
}

private object MusicUrlSource {
    /**
     * @throws IOException
     */
    suspend fun getPlayableUri(music: IMusic): String? {
        if (music is Music) {
            return with(music) {
                when (type) {
                    MusicType.LOCAL -> getLocalMusicUri()
                    MusicType.NETEASE, MusicType.NETEASE_FM -> getNeteaseMusicUri()
                }
            }
        } else {
            return music.getUrl()
        }
    }


    private suspend fun Music.getNeteaseMusicUri(): String? {
        val uris = playUri.filter { it.isValid() }.sortedByDescending { it.bitrate }
        if (uris.isNotEmpty()) {
            return uris[0].uri
        }
        val cloudMusicApi = NeteaseCloudMusicApi()
        val datum = retryNull {
            cloudMusicApi.getMusicUrl(id)
        } ?: return null
        val url = datum.url ?: return null
        playUri.clear()
        playUri.add(MusicUri(datum.bitrate, url, System.currentTimeMillis() + 12_000, datum.md5))
        return url
    }


    private fun Music.getLocalMusicUri(): String? {
        if (playUri.isEmpty()) {
            return null
        }
        val uri = playUri[0].uri
        if (uri.startsWith("file", true)) {
            val file = File(URI(uri))
            if (!file.exists()) {
                throw FileNotFoundException("can not find file : ${file.path}")
            }
            return uri
        }
        throw IOException("error uri")
    }

}