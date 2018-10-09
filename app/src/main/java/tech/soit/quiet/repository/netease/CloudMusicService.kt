package tech.soit.quiet.repository.netease

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

/**
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/22
 * desc   : 网易云音乐的API
 */
interface CloudMusicService {


    @FormUrlEncoded
    @POST("/weapi/search/get")
    fun searchMusic(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/v3/song/detail")
    fun musicDetail(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/song/enhance/player/url")
    fun musicUrl(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/song/lyric?os=osx&lv=-1&kv=-1&tv=-1")
    fun lyric(@Query("id") id: Long, @FieldMap request: Map<String, String>): Call<JsonObject>

    /**
     * login 不进行缓存
     */
    @Headers("Cache-Control: max-age=0")
    @FormUrlEncoded
    @POST("/weapi/login/cellphone")
    fun login(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/user/playlist")
    fun userPlayList(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/v1/discovery/recommend/songs")
    fun recommendSongs(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/point/dailyTask")
    fun dailySign(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/v1/user/detail/{id}")
    fun userDetail(@Path("id") id: Long, @FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/v1/discovery/recommend/resource")
    fun recommendPlaylist(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/personalized/mv")
    fun recommendMv(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/v3/playlist/detail")
    fun playlistDetail(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/mv/detail")
    fun mvDetail(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/v1/radio/get")
    fun personalFm(@FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/radio/like?alg=itembased&time=25")
    fun like(@Query("trackId") id: Long, @Query("like") like: Boolean, @FieldMap request: Map<String, String>): Call<JsonObject>

    @FormUrlEncoded
    @POST("/weapi/radio/trash/add?alg=RT&time=25")
    fun fmTrash(@Query("songId") id: Long, @FieldMap request: Map<String, String>): Call<JsonObject>
}