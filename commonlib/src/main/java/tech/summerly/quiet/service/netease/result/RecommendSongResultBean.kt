package tech.summerly.quiet.service.netease.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/24
 * desc   :
 */
data class RecommendSongResultBean(

        @SerializedName("code")
        @Expose
        val code: Int,

        @SerializedName("recommend")
        @Expose
        val recommend: List<Recommend>? = null
) {
    data class Recommend(

            val name: String,

            @SerializedName("id")
            @Expose
            val id: Long,

//            @SerializedName("alias")
//            @Expose
//            val alias: List<String>? = null,

            @SerializedName("artists")
            @Expose
            val artists: List<Artist>? = null,
            @SerializedName("album")
            @Expose
            val album: Album,
            @SerializedName("duration")
            @Expose
            val duration: Int,
            @SerializedName("commentThreadId")
            @Expose
            val commentThreadId: String? = null,
            @SerializedName("hMusic")
            @Expose
            val hMusic: MusicQuality? = null,
            @SerializedName("mMusic")
            @Expose
            val mMusic: MusicQuality? = null,
            @SerializedName("lMusic")
            @Expose
            val lMusic: MusicQuality? = null,
            @SerializedName("bMusic")
            @Expose
            val bMusic: MusicQuality? = null,
            @SerializedName("mvid")
            @Expose
            val mvid: Long? = null,
            @SerializedName("reason")
            @Expose
            val reason: String? = null
//            @SerializedName("position")
//            @Expose
//            val position: Long? = null,
//            @SerializedName("status")
//            @Expose
//            val status: Long? = null,
//            @SerializedName("fee")
//            @Expose
//            val fee: Long? = null,
//            @SerializedName("copyrightId")
//            @Expose
//            val copyrightId: Long? = null,
//            @SerializedName("disc")
//            @Expose
//            val disc: String? = null,
//            @SerializedName("no")
//            @Expose
//            val no: Long? = null,
//            @SerializedName("starred")
//            @Expose
//            val starred: Boolean? = null,
//            @SerializedName("popularity")
//            @Expose
//            val popularity: Double? = null,
//            @SerializedName("score")
//            @Expose
//            val score: Long? = null,
//            @SerializedName("starredNum")
//            @Expose
//            val starredNum: Long? = null,
//            @SerializedName("playedNum")
//            @Expose
//            val playedNum: Long? = null,
//            @SerializedName("dayPlays")
//            @Expose
//            val dayPlays: Long? = null,
//            @SerializedName("hearTime")
//            @Expose
//            val hearTime: Long? = null,
//            @SerializedName("ringtone")
//            @Expose
//            val ringtone: Any? = null,
//            @SerializedName("crbt")
//            @Expose
//            val crbt: Any? = null,
//            @SerializedName("audition")
//            @Expose
//            val audition: Any? = null,
//            @SerializedName("copyFrom")
//            @Expose
//            val copyFrom: String? = null,
//            @SerializedName("rtUrl")
//            @Expose
//            val rtUrl: Any? = null,
//            @SerializedName("ftype")
//            @Expose
//            val ftype: Long? = null,
//            @SerializedName("rtUrls")
//            @Expose
//            val rtUrls: List<Any>? = null,
//            @SerializedName("copyright")
//            @Expose
//            val copyright: Long? = null,
//            @SerializedName("rtype")
//            @Expose
//            val rtype: Long? = null,
//            @SerializedName("mp3Url")
//            @Expose
//            val mp3Url: Any? = null,
//            @SerializedName("rurl")
//            @Expose
//            val rurl: Any? = null,
//            @SerializedName("privilege")
//            @Expose
//            val privilege: Privilege? = null,
//            @SerializedName("alg")
//            @Expose
//            val alg: String? = null

    )

    data class Album(

            val name: String,
            @SerializedName("id")
            @Expose
            val id: Long,
            @SerializedName("type")
            @Expose
            val type: String,
            @SerializedName("size")
            @Expose
            val size: Long,
            @SerializedName("picId")
            @Expose
            val picId: Long? = null,
            @SerializedName("blurPicUrl")
            @Expose
            val blurPicUrl: String? = null,
//            @SerializedName("companyId")
//            @Expose
//            val companyId: Long? = null,
//            @SerializedName("pic")
//            @Expose
//            val pic: Long? = null,
            @SerializedName("picUrl")
            @Expose
            val picUrl: String? = null,
            @SerializedName("publishTime")
            @Expose
            val publishTime: Long? = null,
//            @SerializedName("description")
//            @Expose
//            val description: String? = null,
//            @SerializedName("tags")
//            @Expose
//            val tags: String? = null,
//            @SerializedName("company")
//            @Expose
//            val company: Any? = null,
//            @SerializedName("briefDesc")
//            @Expose
//            val briefDesc: String? = null,
//            @SerializedName("artist")
//            @Expose
//            val artist: Artist_? = null,
//            @SerializedName("songs")
//            @Expose
//            val songs: List<Any>? = null,
//            @SerializedName("alias")
//            @Expose
//            val alias: List<Any>? = null,
//            @SerializedName("status")
//            @Expose
//            val status: Long? = null,
//            @SerializedName("copyrightId")
//            @Expose
//            val copyrightId: Long? = null,
//            @SerializedName("commentThreadId")
//            @Expose
//            val commentThreadId: String? = null,
            @SerializedName("artists")
            @Expose
            val artists: List<Artist__>? = null
//            @SerializedName("picId_str")
//            @Expose
//            val picIdStr: String? = null

    )

    data class Artist(

            val name: String,
            @SerializedName("id")
            @Expose
            val id: Long,

            @SerializedName("picUrl")
            @Expose
            val picUrl: String? = null,

            @SerializedName("img1v1Url")
            @Expose
            val img1v1Url: String? = null

//            @SerializedName("picId")
//            @Expose
//            val picId: Long? = null,
//            @SerializedName("img1v1Id")
//            @Expose
//            val img1v1Id: Long? = null,
//            @SerializedName("briefDesc")
//            @Expose
//            val briefDesc: String? = null,
//            @SerializedName("albumSize")
//            @Expose
//            val albumSize: Long? = null,
//            @SerializedName("alias")
//            @Expose
//            val alias: List<Any>? = null
//            @SerializedName("trans")
//            @Expose
//            val trans: String? = null,
//            @SerializedName("musicSize")
//            @Expose
//            val musicSize: Long? = null

    )



    data class Artist__(

            val name: String? = null,
            @SerializedName("id")
            @Expose
            val id: Long? = null,
//            @SerializedName("picId")
//            @Expose
//            val picId: Long? = null,
//            @SerializedName("img1v1Id")
//            @Expose
//            val img1v1Id: Long? = null,
//            @SerializedName("briefDesc")
//            @Expose
//            val briefDesc: String? = null,
            @SerializedName("picUrl")
            @Expose
            val picUrl: String? = null,
            @SerializedName("img1v1Url")
            @Expose
            val img1v1Url: String? = null
//            @SerializedName("albumSize")
//            @Expose
//            val albumSize: Long? = null,
//            @SerializedName("alias")
//            @Expose
//            val alias: List<Any>? = null,
//            @SerializedName("trans")
//            @Expose
//            val trans: String? = null,
//            @SerializedName("musicSize")
//            @Expose
//            val musicSize: Long? = null

    )

    data class MusicQuality(
            @SerializedName("id")
            @Expose
            val id: Long,
            @SerializedName("size")
            @Expose
            val size: Long? = null,
            @SerializedName("extension")
            @Expose
            val extension: String? = null,
            @SerializedName("sr")
            @Expose
            val sr: Long? = null,
            @SerializedName("bitrate")
            @Expose
            val bitrate: Long? = null,
            @SerializedName("playTime")
            @Expose
            val playTime: Long? = null,
            @SerializedName("volumeDelta")
            @Expose
            val volumeDelta: Double? = null,
            @SerializedName("dfsId_str")
            @Expose
            val dfsIdStr: Any? = null

            //            @SerializedName("title")
//            @Expose
//            val title: String? = null,
//            @SerializedName("dfsId")
//            @Expose
//            val dfsId: Long? = null,
    )

//    data class Artist_(
//
//            @SerializedName("title")
//            @Expose
//            val title: String? = null,
//            @SerializedName("id")
//            @Expose
//            val id: Long? = null,
//            @SerializedName("picId")
//            @Expose
//            val picId: Long? = null,
//            @SerializedName("img1v1Id")
//            @Expose
//            val img1v1Id: Long? = null,
//            @SerializedName("briefDesc")
//            @Expose
//            val briefDesc: String? = null,
//            @SerializedName("picUrl")
//            @Expose
//            val picUrl: String? = null,
//            @SerializedName("img1v1Url")
//            @Expose
//            val img1v1Url: String? = null,
//            @SerializedName("albumSize")
//            @Expose
//            val albumSize: Long? = null,
//            @SerializedName("alias")
//            @Expose
//            val alias: List<Any>? = null,
//            @SerializedName("trans")
//            @Expose
//            val trans: String? = null,
//            @SerializedName("musicSize")
//            @Expose
//            val musicSize: Long? = null
//
//    )

//data class Privilege (
//
//    @SerializedName("id")
//    @Expose
//    val id: Long? = null,
//    @SerializedName("fee")
//    @Expose
//    val fee: Long? = null,
//    @SerializedName("payed")
//    @Expose
//    val payed: Long? = null,
//    @SerializedName("st")
//    @Expose
//    val st: Long? = null,
//    @SerializedName("pl")
//    @Expose
//    val pl: Long? = null,
//    @SerializedName("dl")
//    @Expose
//    val dl: Long? = null,
//    @SerializedName("sp")
//    @Expose
//    val sp: Long? = null,
//    @SerializedName("cp")
//    @Expose
//    val cp: Long? = null,
//    @SerializedName("subp")
//    @Expose
//    val subp: Long? = null,
//    @SerializedName("cs")
//    @Expose
//    val cs: Boolean? = null,
//    @SerializedName("maxbr")
//    @Expose
//    val maxbr: Long? = null,
//    @SerializedName("fl")
//    @Expose
//    val fl: Long? = null,
//    @SerializedName("toast")
//    @Expose
//    val toast: Boolean? = null,
//    @SerializedName("flag")
//    @Expose
//    val flag: Long? = null,
//
//)
}

