package tech.soit.quiet.repository.netease

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import tech.soit.quiet.utils.component.network.enqueue
import tech.soit.quiet.utils.component.support.Resource

object NeteaseRepository {


    private val service = CloudMusicServiceProvider.provideCloudMusicService()

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
    fun searchMusic(keyword: String, offset: Int = 0, limit: Int = 30) {
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


    fun musicUrl(bitrate: Int = 999000, vararg ids: Long) {
        val encrypt = Crypto.encrypt("""
        {
            "ids" : ["${ids.joinToString(",")}"],
            "br" : $bitrate,
            "csrf_token" : ""
        }
        """.trimIndent()
        )
    }

    fun login(phone: String, password: String) {
        val encrypt = Crypto.encrypt("""
            {
                "phone" : "$phone",
                "password" : "${with(Crypto) { password.md5() }}",
                "rememberLogin" : "true"
            }
        """.trimIndent())
        TODO()
    }


    fun getUserPlayerList(userId: Long, offset: Int = 0, limit: Int = 1000) {
        val encrypt = Crypto.encrypt("""
            {
                "offset" : $offset ,
                "uid" : "$userId",
                "limit" : $limit ,
                "csrf_token" : ""
            }
        """.trimIndent())

        val liveData = MutableLiveData<Resource<JsonObject>>()
        liveData.postValue(Resource.loading())
        service.userPlayList(encrypt).enqueue {
            onFailure { call, t ->
                liveData.postValue(Resource.error(t.message!!))
            }
            onResponse { call, response ->
                liveData.postValue(Resource.success(response.body()))
            }
        }
    }

    fun recommendSongs() {
        val encrypt = Crypto.encrypt("""
            {"offset":0,"total":true,"limit":20,"csrf_token":""}
        """.trimIndent())
        TODO()
    }


    fun playListDetail(playlistId: Long, offset: Int = 0) {
        val encrypt = Crypto.encrypt("""
            {
                "id":"$playlistId",
                "offset":$offset,
                "total":true,
                "limit":1000,
                "n":1000s,
                "csrf_token":""
            }
        """.trimIndent())
        TODO()
    }


    fun personalFm() {
        val params = Crypto.encrypt("""
            {"csrf_token":""}
        """.trimIndent())
        TODO()
    }

    /**
     * Boolean 为 true 代表操作（喜欢或者移除喜欢）成功
     */
    fun like(id: Long, like: Boolean = true) {
        val params = Crypto.encrypt("""
            {
                "csrf_token" : "",
                "trackId" : "$id",
                "like" : $like
            }
        """.trimIndent())
        TODO()
    }

}