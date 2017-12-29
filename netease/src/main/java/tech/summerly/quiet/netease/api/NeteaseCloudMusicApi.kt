package tech.summerly.quiet.netease.api

import android.content.Context
import kotlinx.coroutines.experimental.CancellationException
import okhttp3.Cache
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicUri
import tech.summerly.quiet.commonlib.cookie.PersistentCookieStore
import tech.summerly.quiet.commonlib.utils.await
import tech.summerly.quiet.commonlib.utils.md5
import tech.summerly.quiet.netease.api.bean.MusicSearchResult
import tech.summerly.quiet.netease.api.converter.Crypto
import tech.summerly.quiet.netease.api.converter.NeteaseResultMapper
import tech.summerly.quiet.netease.api.result.LoginResultBean
import tech.summerly.quiet.netease.api.result.MusicDetailResultBean
import tech.summerly.quiet.netease.api.result.MusicUrlResultBean

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

    private val mapper = NeteaseResultMapper()

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
    suspend fun searchMusic(keyword: String, offset: Int = 0, limit: Int = 30): MusicSearchResult {
        val params = Crypto.encrypt("""
            {
                "csrf_token" : "",
                "limit" : $limit ,
                "type" : 1 ,
                "s" : "$keyword",
                "offset" : $offset
            }
        """.trimIndent())
        TODO()
    }

    suspend fun getMusicDetail(id: Long): MusicDetailResultBean {
        val encrypt = Crypto.encrypt("""
            {
                "c"  : "[{\"id\" : $id}]",
                "ids": "[$id]",
                "csrf_token": ""
            }
        """.trimIndent())
        return neteaseService.musicDetail(encrypt).await()
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

    suspend fun getMusicUrl(id: Long, bitrate: Int = 999000): String? {
        return getMusicUrl(ids = *longArrayOf(id), bitrate = bitrate).find { it.id == id }?.url
    }

    suspend fun getLyric(id: Long): String? {
        return neteaseService.lyric(id, Crypto.encrypt("{}")).await().lrc.lyric
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
        val data = neteaseService.musicUrl(encrypt).await().data?.get(0) ?: error("fetch url failed")
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
}