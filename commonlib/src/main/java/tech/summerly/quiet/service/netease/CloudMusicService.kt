package tech.summerly.quiet.service.netease

import kotlinx.coroutines.experimental.Deferred
import retrofit2.Call
import retrofit2.http.*
import tech.summerly.quiet.service.netease.result.*

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/22
 * desc   : 网易云音乐的API,使用 Retrofit2
 */
internal interface CloudMusicService {

    @FormUrlEncoded
    @POST("/weapi/search/get")
    fun searchMusic(@FieldMap request: Map<String, String>): Deferred<MusicSearchResultBean>

    @FormUrlEncoded
    @POST("/weapi/v3/song/detail")
    fun musicDetail(@FieldMap request: Map<String, String>): Deferred<MusicDetailResultBean>

    @FormUrlEncoded
    @POST("/weapi/song/enhance/player/url")
    fun musicUrl(@FieldMap request: Map<String, String>): Call<MusicUrlResultBean>

    @FormUrlEncoded
    @POST("/weapi/song/lyric?os=osx&lv=-1&kv=-1&tv=-1")
    fun lyric(@Query("id") id: Long, @FieldMap request: Map<String, String>): Call<LyricResultBean>

    /**
     * login 不进行缓存
     */
    @Headers("Cache-Control: max-age=0")
    @FormUrlEncoded
    @POST("/weapi/login/cellphone")
    fun login(@FieldMap request: Map<String, String>): Call<LoginResultBean>

    @FormUrlEncoded
    @POST("/weapi/user/playlist")
    fun userPlayList(@FieldMap request: Map<String, String>): Deferred<PlaylistResultBean>

    @FormUrlEncoded
    @POST("/weapi/v1/discovery/recommend/songs")
    fun recommendSongs(@FieldMap request: Map<String, String>): Deferred<RecommendSongResultBean>

    @FormUrlEncoded
    @POST("/weapi/point/dailyTask")
    fun dailySign(@FieldMap request: Map<String, String>): Call<DailySignResultBean>

    @FormUrlEncoded
    @POST("/weapi/v1/user/detail/{id}")
    fun userDetail(@Path("id") id: Long, @FieldMap request: Map<String, String>): Call<UserDetailResultBean>

    @FormUrlEncoded
    @POST("/weapi/v1/discovery/recommend/resource")
    fun recommendPlaylist(@FieldMap request: Map<String, String>): Call<RecommendPlaylistResultBean>

    @FormUrlEncoded
    @POST("/weapi/personalized/mv")
    fun recommendMv(@FieldMap request: Map<String, String>): Call<RecommendMvResultBean>

    @FormUrlEncoded
    @POST("/weapi/v3/playlist/detail")
    fun playlistDetail(@FieldMap request: Map<String, String>): Call<PlaylistDetailResultBean>

    @FormUrlEncoded
    @POST("/weapi/mv/detail")
    fun mvDetail(@FieldMap request: Map<String, String>): Call<MvDetailResultBean>

    @FormUrlEncoded
    @POST("/weapi/v1/radio/get")
    fun personalFm(@FieldMap request: Map<String, String>): Call<PersonalFmDataResult>

    @FormUrlEncoded
    @POST("/weapi/radio/like?alg=itembased&time=25")
    fun like(@Query("trackId") id: Long, @Query("like") like: Boolean, @FieldMap request: Map<String, String>): Call<CommonResultBean>

    @FormUrlEncoded
    @POST("/weapi/radio/trash/add?alg=RT&time=25")
    fun fmTrash(@Query("songId") id: Long, @FieldMap request: Map<String, String>): Call<CommonResultBean>

    @FormUrlEncoded
    @POST("/weapi/v1/play/record")
    fun record(@FieldMap request: Map<String, String>): Deferred<RecordResultBean>
}