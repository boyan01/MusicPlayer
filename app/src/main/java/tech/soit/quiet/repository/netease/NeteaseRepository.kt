package tech.soit.quiet.repository.netease

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import tech.soit.quiet.model.po.NeteasePlayList
import tech.soit.quiet.model.vo.PlayList
import tech.soit.quiet.utils.component.network.enqueue
import tech.soit.quiet.utils.component.support.Resource
import tech.soit.quiet.utils.component.support.Status
import tech.soit.quiet.utils.component.support.map
import java.util.concurrent.atomic.AtomicBoolean

class NeteaseRepository(
        private val service: CloudMusicService = CloudMusicServiceProvider.provideCloudMusicService()
) {

    companion object {

        val instance = NeteaseRepository()

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


    fun getUserPlayerList(userId: Long, offset: Int = 0, limit: Int = 1000): LiveData<Resource<List<PlayList>>> {
        val encrypt = Crypto.encrypt("""
            {
                "offset" : $offset ,
                "uid" : "$userId",
                "limit" : $limit ,
                "csrf_token" : ""
            }
        """.trimIndent())
        return service.userPlayList(encrypt)
                .preParse<JsonArray>("playlist")
                .mapJsonArray { NeteasePlayList(it as JsonObject) }
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


    private fun <T> LiveData<Resource<JsonArray>>.mapJsonArray(transform: (JsonElement) -> T): LiveData<Resource<List<T>>> {
        return map { resource ->
            when {
                resource == null -> null
                resource.status == Status.SUCCESS -> {
                    val result = resource.data!!.map(transform)
                    Resource.success(result)
                }
                resource.status == Status.ERROR -> {
                    Resource.error(resource.message)
                }
                else -> error("")
            }
        }
    }


    /**
     * this function only fit parse json object from Netease remove service
     */
    fun <T : JsonElement> Call<JsonObject>.preParse(
            key: String,
            responseHandler: ((response: JsonObject?, liveData: MutableLiveData<Resource<T>>) -> Unit)? = null
    ): LiveData<Resource<T>> {
        return object : MutableLiveData<Resource<T>>() {

            private val started = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                val data = this
                if (started.compareAndSet(false, true)) {
                    enqueue {
                        onFailure { _, t ->
                            postValue(Resource.error(t))
                        }
                        onResponse { _, response ->
                            val body = response.body()
                            when {
                                responseHandler != null -> responseHandler(body, data)
                                body == null -> postValue(Resource.error("service not responding"))
                                body["code"].asInt != 200 -> postValue(Resource.error("service error : ${body["code"].asInt}"))
                                else -> @Suppress("UNCHECKED_CAST")
                                postValue(Resource.success(body[key] as T))
                            }
                        }
                    }
                }
            }
        }
    }

}