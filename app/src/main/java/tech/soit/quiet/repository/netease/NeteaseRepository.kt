package tech.soit.quiet.repository.netease

import com.google.gson.JsonObject
import tech.soit.quiet.model.po.NeteasePlayList
import tech.soit.quiet.model.po.NeteaseUser
import tech.soit.quiet.model.vo.PlayList
import tech.soit.quiet.model.vo.User
import tech.soit.quiet.utils.component.persistence.KeyValue
import tech.soit.quiet.utils.component.persistence.get
import tech.soit.quiet.utils.testing.OpenForTesting

@OpenForTesting
class NeteaseRepository(
        private val service: CloudMusicService = CloudMusicServiceProvider.provideCloudMusicService()
) {

    companion object {

        val instance = NeteaseRepository()

        private const val KEY_USER = "netease_repository_user"

        private const val REMOTE_KEY_MESSAGE = "msg"

    }

    /**
     * @return null when no user login
     */
    fun getLoginUser(): User? {
        return KeyValue.get<NeteaseUser>(KEY_USER)
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

    suspend fun login(phone: String, password: String): User {
        val encrypt = Crypto.encrypt("""
            {
                "phone" : "$phone",
                "password" : "${with(Crypto) { password.md5() }}",
                "rememberLogin" : "true"
            }
        """.trimIndent())
        val response = service.login(encrypt).await()
        if (!response.isSuccess()) {
            error(response[REMOTE_KEY_MESSAGE])
        }

        val profile = response["profile"].asJsonObject
        val user = NeteaseUser(
                id = response["account"].asJsonObject["id"].asLong,
                nickname = profile["nickname"].asString,
                avatarUrl = profile["avatarUrl"].asString
        )
        KeyValue.put(KEY_USER, user)
        return user
    }


    suspend fun getUserPlayerList(userId: Long, offset: Int = 0, limit: Int = 1000): List<PlayList> {
        val encrypt = Crypto.encrypt("""
            {
                "offset" : $offset ,
                "uid" : "$userId",
                "limit" : $limit ,
                "csrf_token" : ""
            }
        """.trimIndent())
        val response = service.userPlayList(encrypt).await()
        if (!response.isSuccess()) {
            error(response["msg"])
        }
        val array = response["playlist"].asJsonArray
        return array.map { NeteasePlayList(it as JsonObject) }
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


    private fun JsonObject.isSuccess(): Boolean {
        return get("code").asInt == 200
    }


}