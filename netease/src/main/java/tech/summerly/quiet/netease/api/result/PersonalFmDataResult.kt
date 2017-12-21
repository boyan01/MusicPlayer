package tech.summerly.quiet.netease.api.result

import kotlinx.serialization.SerialName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/9/22
 * desc   :
 */

data class PersonalFmDataResult(
        @SerialName("popAdjust")
        val popAdjust: Boolean? = null,
        @SerialName("data")
        val data: List<Datum>? = null,
        @SerialName("code")
        val code: Int
) {

    data class Datum(

            @SerialName("title")
            val name: String,
            @SerialName("id")
            val id: Long,
            @SerialName("position")
            val position: Long? = null,
            @SerialName("alias")
            val alias: List<Any>? = null,
            @SerialName("status")
            val status: Long? = null,
            @SerialName("fee")
            val fee: Long? = null,
            @SerialName("copyrightId")
            val copyrightId: Long? = null,
            @SerialName("disc")
            val disc: String? = null,
            @SerialName("no")
            val no: Long? = null,
            @SerialName("artists")
            val artists: List<Artist>? = null,
            @SerialName("album")
            val album: Album? = null,
            @SerialName("starred")
            val starred: Boolean? = null,
            @SerialName("popularity")
            val popularity: Long? = null,
            @SerialName("score")
            val score: Long? = null,
            @SerialName("starredNum")
            val starredNum: Long? = null,
            @SerialName("duration")
            val duration: Int,
            @SerialName("playedNum")
            val playedNum: Long? = null,
            @SerialName("dayPlays")
            val dayPlays: Long? = null,
            @SerialName("hearTime")
            val hearTime: Long? = null,
            @SerialName("ringtone")
            val ringtone: Any? = null,
            @SerialName("crbt")
            val crbt: Any? = null,
            @SerialName("audition")
            val audition: Any? = null,
            @SerialName("commentThreadId")
            val commentThreadId: String? = null,
            @SerialName("mvid")
            val mvid: Long? = null,
            @SerialName("alg")
            val alg: String? = null

    )

    data class Album(

            @SerialName("title")
            val name: String? = null,
            @SerialName("id")
            val id: Long? = null,
            @SerialName("type")
            val type: String? = null,
            @SerialName("size")
            val size: Long? = null,
            @SerialName("picId")
            val picId: Long? = null,
            @SerialName("blurPicUrl")
            val blurPicUrl: String? = null,
            @SerialName("companyId")
            val companyId: Long? = null,
            @SerialName("pic")
            val pic: Long? = null,
            @SerialName("picUrl")
            val picUrl: String? = null,
            @SerialName("publishTime")
            val publishTime: Long? = null,
            @SerialName("description")
            val description: String? = null,
            @SerialName("tags")
            val tags: String? = null,
            @SerialName("company")
            val company: Any? = null,
            @SerialName("briefDesc")
            val briefDesc: String? = null,
            @SerialName("songs")
            val songs: List<Any>? = null,
            @SerialName("alias")
            val alias: List<Any>? = null,
            @SerialName("status")
            val status: Long? = null,
            @SerialName("copyrightId")
            val copyrightId: Long? = null,
            @SerialName("commentThreadId")
            val commentThreadId: String? = null,
            @SerialName("picId_str")
            val picIdStr: String? = null

    )

    data class Artist(

            @SerialName("title")
            val name: String,
            @SerialName("id")
            val id: Long,
            @SerialName("picId")
            val picId: Long? = null,
            @SerialName("img1v1Id")
            val img1v1Id: Long? = null,
            @SerialName("briefDesc")
            val briefDesc: String? = null,
            @SerialName("picUrl")
            val picUrl: String? = null,
            @SerialName("img1v1Url")
            val img1v1Url: String? = null,
            @SerialName("albumSize")
            val albumSize: Long? = null,
            @SerialName("alias")
            val alias: List<Any>? = null,
            @SerialName("trans")
            val trans: String? = null,
            @SerialName("musicSize")
            val musicSize: Long? = null
    )
}