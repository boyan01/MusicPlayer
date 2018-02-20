package tech.summerly.quiet.service.netease

import android.content.Context
import kotlinx.coroutines.experimental.CancellationException
import okhttp3.Cache
import tech.summerly.quiet.commonlib.LibModule
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicUri
import tech.summerly.quiet.commonlib.cookie.PersistentCookieStore
import tech.summerly.quiet.commonlib.objects.PortionList
import tech.summerly.quiet.commonlib.utils.await
import tech.summerly.quiet.commonlib.utils.md5
import tech.summerly.quiet.service.netease.converter.Crypto
import tech.summerly.quiet.service.netease.converter.NeteaseResultMapper
import tech.summerly.quiet.service.netease.result.LoginResultBean
import tech.summerly.quiet.service.netease.result.MusicUrlResultBean
import tech.summerly.quiet.service.netease.result.PlaylistDetailResultBean
import tech.summerly.quiet.service.netease.result.PlaylistResultBean
import java.io.IOException

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/23
 * desc   :
 */
class NeteaseCloudMusicApi {

    private val context: Context get() = LibModule

    private val neteaseService = CloudMusicServiceProvider()
            .provideCloudMusicService(PersistentCookieStore(context.applicationContext),
                    Cache(context.cacheDir, 1024 * 1024))

    private val mapper = NeteaseResultMapper()

    /**
     * search music by keyword
     * @param offset fetch service musics by skip [offset]
     * @param limit max number musics fetch at once
     */
    suspend fun searchMusic(keyword: String, offset: Int = 0, limit: Int = 30): PortionList<Music> {
        val params = buildSearchParams(keyword, offset, limit, 1)
        val (result, code) = neteaseService.searchMusic(params).await()
        if (code == 200) {
            val data = result.songs?.map { mapper.convertToMusic(it) } ?: emptyList()
            return PortionList(data.toMutableList(), result.songCount, offset)
        } else {
            throw IOException("remote service error code : $code")
        }
    }

    /**
     * 搜索服务
     * type: 1: 单曲
     *       10: 专辑
     *       100: 歌手
     *       1000: 歌单
     *       1002: 用户
     *       1004: MV
     *       1006: 歌词
     *       1009: 电台
     */
    private fun buildSearchParams(keyword: String, offset: Int, limit: Int, type: Int = 1): Map<String, String> {
        return Crypto.encrypt("""
            {
                "csrf_token" : "",
                "limit" : $limit ,
                "type" : $type ,
                "s" : "$keyword",
                "offset" : $offset
            }
        """.trimIndent())
    }

    suspend fun getMusicDetail(id: Long): Music {
        val encrypt = Crypto.encrypt("""
            {
                "c"  : "[{\"id\" : $id}]",
                "ids": "[$id]",
                "csrf_token": ""
            }
        """.trimIndent())
        val (songs, code) = neteaseService.musicDetail(encrypt).await()
        if (code == 301) {
            error("need login")
        }
        if (songs == null || songs.isEmpty()) {
            error("error response")
        }
        return songs.map { mapper.convertToMusic(it) }[0]
    }

    /**
     * [ids] 歌曲id
     * [bitrate] 比特率
     */
    suspend fun getMusicUrl(vararg ids: Long, bitrate: Int = 999000): List<MusicUrlResultBean.Datum> {
        val encrypt = Crypto.encrypt("""
        {
            "ids" : ["${ids.joinToString(",")}"],
            "br" : $bitrate,
            "csrf_token" : ""
        }
        """.trimIndent()
        )
        return neteaseService.musicUrl(encrypt).await().data ?: emptyList()
    }

    suspend fun getMusicUrl(id: Long, bitrate: Int = 999000): MusicUrlResultBean.Datum? {
        return getMusicUrl(ids = *longArrayOf(id), bitrate = bitrate).find { it.id == id }
    }

    suspend fun getLyric(id: Long): String? {
        return neteaseService.lyric(id, Crypto.encrypt("{}")).await().lrc?.lyric
    }

    suspend fun login(phone: String, password: String): LoginResultBean {
        val encrypt = Crypto.encrypt("""
            {
                "phone" : "$phone",
                "password" : "${password.md5()}",
                "rememberLogin" : "true"
            }
        """.trimIndent())
        return neteaseService.login(encrypt).await()
    }

    suspend fun getFmMusics(): List<Music> {
        val params = Crypto.encrypt("""
            {"csrf_token":""}
        """.trimIndent())
        val personalFmDataResult = neteaseService.personalFm(params).await()
        if (personalFmDataResult.code != 200) {
            throw CancellationException("fetch fm musics failed!")
        }
        return personalFmDataResult.data?.map {
            mapper.convertToMusic(it)
        } ?: emptyList()
    }

    /**
     * 获取指定ID的音乐的播放链接
     */
    suspend fun musicUrl(id: Long, bitrate: Int = 999000): MusicUri {
        val encrypt = Crypto.encrypt("""
        {
            "ids" : ["$id"],
            "br" : $bitrate,
            "csrf_token" : ""
        }
        """.trimIndent()
        )
        val data = neteaseService.musicUrl(encrypt).await().data?.get(0)
                ?: error("fetch url failed")
        if (data.url == null) {
            error("fetch url failed")
        }
        return MusicUri(
                data.bitrate,
                data.url!!,
                System.currentTimeMillis() + 10 * 1000 * 60,
                data.md5
        )
    }

    suspend fun getUserPlaylists(userId: Long, offset: Int = 0, limit: Int = 1000): List<PlaylistResultBean.PlaylistBean> {
        val encrypt = Crypto.encrypt("""
            {
                "offset" : $offset ,
                "uid" : "$userId",
                "limit" : $limit ,
                "csrf_token" : ""
            }
        """.trimIndent())
        val result = neteaseService.userPlayList(encrypt).await()
        if (result.code != 200) {
            error("error response!")
        }
        return result.playlist ?: emptyList()
    }

    suspend fun getDailyRecommend(): List<Music> {
        val encrypt = Crypto.encrypt("""
            {"offset":0,"total":true,"limit":20,"csrf_token":""}
        """.trimIndent())
        val recommend = neteaseService.recommendSongs(encrypt).await()
        if (recommend.code == 301) {
            //need login
            error("please login first!")
        }
        if (recommend.code != 200 && recommend.recommend?.isEmpty() != false) {
            error("error response")
        }
        recommend.recommend!!
        return recommend.recommend.map {
            mapper.convertToMusic(it)
        }
    }

    suspend fun getPlaylistDetail(playlistId: Long): Pair<PlaylistDetailResultBean.Playlist, List<Music>> {
        val encrypt = Crypto.encrypt("""
            {
                "id":"$playlistId",
                "n":1000000,
                "csrf_token":""
            }
        """.trimIndent())
        val playlistDetailBean = neteaseService.playlistDetail(encrypt).await()
        if (playlistDetailBean.code == 301) {
            error("please login first")
        }
        if (playlistDetailBean.code != 200 || playlistDetailBean.playlist == null) {
            error("error response")
        }
        return playlistDetailBean.playlist to
                (playlistDetailBean.playlist.tracks?.map { mapper.convertToMusic(it) }
                        ?: emptyList())
    }
}
