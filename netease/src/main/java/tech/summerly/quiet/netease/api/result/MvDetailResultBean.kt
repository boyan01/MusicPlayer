package tech.summerly.quiet.netease.api.result

import kotlinx.serialization.SerialName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/9/8
 * desc   :
 */
class MvDetailResultBean(
        @SerialName("data")
        val data: Data? = null,

        @SerialName("code")
        val code: Int

) {

    data class Brs(
            @SerialName("1080")
            val _1080: String? = null,
            @SerialName("720")
            val _720: String? = null,
            @SerialName("480")
            val _480: String? = null,
            @SerialName("240")
            val _240: String? = null

    )

    data class Data(

            @SerialName("id")
            val id: Long,

            @SerialName("title")
            val name: String? = null,

            @SerialName("artistId")
            val artistId: Long? = null,

            @SerialName("artistName")
            val artistName: String? = null,

            @SerialName("cover")
            val cover: String? = null,

            @SerialName("playCount")
            val playCount: Long? = null,

            @SerialName("subCount")
            val subCount: Long? = null,

            @SerialName("shareCount")
            val shareCount: Long? = null,

            @SerialName("likeCount")
            val likeCount: Long? = null,

            @SerialName("commentCount")
            val commentCount: Long? = null,

            @SerialName("duration")
            val duration: Long,

            @SerialName("publishTime")
            val publishTime: String? = null,

            @SerialName("brs")
            val brs: Brs? = null,

            @SerialName("commentThreadId")
            val commentThreadId: String? = null
    )
}