package tech.summerly.quiet.data.netease.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/9/8
 * desc   :
 */
class MvDetailResultBean(
        @SerializedName("data")
        @Expose
        val data: Data? = null,

        @SerializedName("code")
        @Expose
        val code: Int

) {

    data class Brs(
            @SerializedName("1080")
            @Expose
            val _1080: String? = null,
            @SerializedName("720")
            @Expose
            val _720: String? = null,
            @SerializedName("480")
            @Expose
            val _480: String? = null,
            @SerializedName("240")
            @Expose
            val _240: String? = null

    )

    data class Data(

            @SerializedName("id")
            @Expose
            val id: Long,

            @SerializedName("title")
            @Expose
            val name: String? = null,

            @SerializedName("artistId")
            @Expose
            val artistId: Long? = null,

            @SerializedName("artistName")
            @Expose
            val artistName: String? = null,

            @SerializedName("cover")
            @Expose
            val cover: String? = null,

            @SerializedName("playCount")
            @Expose
            val playCount: Long? = null,

            @SerializedName("subCount")
            @Expose
            val subCount: Long? = null,

            @SerializedName("shareCount")
            @Expose
            val shareCount: Long? = null,

            @SerializedName("likeCount")
            @Expose
            val likeCount: Long? = null,

            @SerializedName("commentCount")
            @Expose
            val commentCount: Long? = null,

            @SerializedName("duration")
            @Expose
            val duration: Long,

            @SerializedName("publishTime")
            @Expose
            val publishTime: String? = null,

            @SerializedName("brs")
            @Expose
            val brs: Brs? = null,

            @SerializedName("commentThreadId")
            @Expose
            val commentThreadId: String? = null
    )
}