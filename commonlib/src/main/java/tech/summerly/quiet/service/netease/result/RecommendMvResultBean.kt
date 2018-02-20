package tech.summerly.quiet.service.netease.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/25
 * desc   :
 */
data class RecommendMvResultBean(
        @SerializedName("code")
        @Expose
        val code: Long,
        @SerializedName("category")
        @Expose
        val category: Long? = null,
        @SerializedName("result")
        @Expose
        val result: List<Result>? = null
) {
//    data class Artist(
//            @SerializedName("id")
//            @Expose
//            val id: Long,
//            @SerializedName("title")
//            @Expose
//            val title: String
//    )

    data class Result(

            @SerializedName("id")
            @Expose
            val id: Long,

            //            @SerializedName("type")
//            @Expose
//            val type: Long? = null,
            @SerializedName("title")
            @Expose
            val name: String,

            @SerializedName("copywriter")
            @Expose
            val copywriter: String? = null,

            @SerializedName("picUrl")
            @Expose
            val picUrl: String? = null,

            //            @SerializedName("canDislike")
//            @Expose
//            val canDislike: Boolean? = null,
            @SerializedName("duration")
            @Expose
            val duration: Long? = null,
            @SerializedName("playCount")
            @Expose
            val playCount: Long? = null,
            //            @SerializedName("subed")
//            @Expose
//            val subed: Boolean? = null,
//            @SerializedName("artists")
//            @Expose
//            val artists: List<Artist>? = null,
            @SerializedName("artistName")
            @Expose
            val artistName: String? = null,
            @SerializedName("artistId")
            @Expose
            val artistId: Long? = null
            //            @SerializedName("alg")
//            @Expose
//            val alg: String? = null

    )
}
