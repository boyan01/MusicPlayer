package tech.summerly.quiet.data.netease.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/9/22
 * desc   :
 */

data class PersonalFmDataResult(
        @SerializedName("popAdjust")
        @Expose
        val popAdjust: Boolean? = null,
        @SerializedName("data")
        @Expose
        val data: List<Datum>? = null,
        @SerializedName("code")
        @Expose
        val code: Int
) {

    data class Datum(

            @SerializedName("title")
            @Expose
            val name: String,
            @SerializedName("id")
            @Expose
            val id: Long,
            @SerializedName("position")
            @Expose
            val position: Long? = null,
            @SerializedName("alias")
            @Expose
            val alias: List<Any>? = null,
            @SerializedName("status")
            @Expose
            val status: Long? = null,
            @SerializedName("fee")
            @Expose
            val fee: Long? = null,
            @SerializedName("copyrightId")
            @Expose
            val copyrightId: Long? = null,
            @SerializedName("disc")
            @Expose
            val disc: String? = null,
            @SerializedName("no")
            @Expose
            val no: Long? = null,
            @SerializedName("artists")
            @Expose
            val artists: List<Artist>? = null,
            @SerializedName("album")
            @Expose
            val album: Album? = null,
            @SerializedName("starred")
            @Expose
            val starred: Boolean? = null,
            @SerializedName("popularity")
            @Expose
            val popularity: Long? = null,
            @SerializedName("score")
            @Expose
            val score: Long? = null,
            @SerializedName("starredNum")
            @Expose
            val starredNum: Long? = null,
            @SerializedName("duration")
            @Expose
            val duration: Int,
            @SerializedName("playedNum")
            @Expose
            val playedNum: Long? = null,
            @SerializedName("dayPlays")
            @Expose
            val dayPlays: Long? = null,
            @SerializedName("hearTime")
            @Expose
            val hearTime: Long? = null,
            @SerializedName("ringtone")
            @Expose
            val ringtone: Any? = null,
            @SerializedName("crbt")
            @Expose
            val crbt: Any? = null,
            @SerializedName("audition")
            @Expose
            val audition: Any? = null,
            @SerializedName("commentThreadId")
            @Expose
            val commentThreadId: String? = null,
            @SerializedName("mvid")
            @Expose
            val mvid: Long? = null,
            @SerializedName("alg")
            @Expose
            val alg: String? = null

    )

    data class Album(

            @SerializedName("title")
            @Expose
            val name: String? = null,
            @SerializedName("id")
            @Expose
            val id: Long? = null,
            @SerializedName("type")
            @Expose
            val type: String? = null,
            @SerializedName("size")
            @Expose
            val size: Long? = null,
            @SerializedName("picId")
            @Expose
            val picId: Long? = null,
            @SerializedName("blurPicUrl")
            @Expose
            val blurPicUrl: String? = null,
            @SerializedName("companyId")
            @Expose
            val companyId: Long? = null,
            @SerializedName("pic")
            @Expose
            val pic: Long? = null,
            @SerializedName("picUrl")
            @Expose
            val picUrl: String? = null,
            @SerializedName("publishTime")
            @Expose
            val publishTime: Long? = null,
            @SerializedName("description")
            @Expose
            val description: String? = null,
            @SerializedName("tags")
            @Expose
            val tags: String? = null,
            @SerializedName("company")
            @Expose
            val company: Any? = null,
            @SerializedName("briefDesc")
            @Expose
            val briefDesc: String? = null,
            @SerializedName("songs")
            @Expose
            val songs: List<Any>? = null,
            @SerializedName("alias")
            @Expose
            val alias: List<Any>? = null,
            @SerializedName("status")
            @Expose
            val status: Long? = null,
            @SerializedName("copyrightId")
            @Expose
            val copyrightId: Long? = null,
            @SerializedName("commentThreadId")
            @Expose
            val commentThreadId: String? = null,
            @SerializedName("picId_str")
            @Expose
            val picIdStr: String? = null

    )

    data class Artist(

            @SerializedName("title")
            @Expose
            val name: String,
            @SerializedName("id")
            @Expose
            val id: Long,
            @SerializedName("picId")
            @Expose
            val picId: Long? = null,
            @SerializedName("img1v1Id")
            @Expose
            val img1v1Id: Long? = null,
            @SerializedName("briefDesc")
            @Expose
            val briefDesc: String? = null,
            @SerializedName("picUrl")
            @Expose
            val picUrl: String? = null,
            @SerializedName("img1v1Url")
            @Expose
            val img1v1Url: String? = null,
            @SerializedName("albumSize")
            @Expose
            val albumSize: Long? = null,
            @SerializedName("alias")
            @Expose
            val alias: List<Any>? = null,
            @SerializedName("trans")
            @Expose
            val trans: String? = null,
            @SerializedName("musicSize")
            @Expose
            val musicSize: Long? = null
    )
}