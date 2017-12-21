package tech.summerly.quiet.netease.api.result

import kotlinx.serialization.SerialName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/24
 * desc   :
 */
data class RecommendSongResultBean(

        @SerialName("code")
        val code: Int,

        @SerialName("recommend")
        val recommend: List<Recommend>? = null
) {
    data class Recommend(

            @SerialName("title")
            val name: String,

            @SerialName("id")
            val id: Long,

//            @SerialName("alias")
//            val alias: List<String>? = null,

            @SerialName("artists")
            val artists: List<Artist>? = null,
            @SerialName("album")
            val album: Album,
            @SerialName("duration")
            val duration: Int,
            @SerialName("commentThreadId")
            val commentThreadId: String? = null,
            @SerialName("hMusic")
            val hMusic: MusicQuality? = null,
            @SerialName("mMusic")
            val mMusic: MusicQuality? = null,
            @SerialName("lMusic")
            val lMusic: MusicQuality? = null,
            @SerialName("bMusic")
            val bMusic: MusicQuality? = null,
            @SerialName("mvid")
            val mvid: Long? = null,
            @SerialName("reason")
            val reason: String? = null
//            @SerialName("position")
//            val position: Long? = null,
//            @SerialName("status")
//            val status: Long? = null,
//            @SerialName("fee")
//            val fee: Long? = null,
//            @SerialName("copyrightId")
//            val copyrightId: Long? = null,
//            @SerialName("disc")
//            val disc: String? = null,
//            @SerialName("no")
//            val no: Long? = null,
//            @SerialName("starred")
//            val starred: Boolean? = null,
//            @SerialName("popularity")
//            val popularity: Double? = null,
//            @SerialName("score")
//            val score: Long? = null,
//            @SerialName("starredNum")
//            val starredNum: Long? = null,
//            @SerialName("playedNum")
//            val playedNum: Long? = null,
//            @SerialName("dayPlays")
//            val dayPlays: Long? = null,
//            @SerialName("hearTime")
//            val hearTime: Long? = null,
//            @SerialName("ringtone")
//            val ringtone: Any? = null,
//            @SerialName("crbt")
//            val crbt: Any? = null,
//            @SerialName("audition")
//            val audition: Any? = null,
//            @SerialName("copyFrom")
//            val copyFrom: String? = null,
//            @SerialName("rtUrl")
//            val rtUrl: Any? = null,
//            @SerialName("ftype")
//            val ftype: Long? = null,
//            @SerialName("rtUrls")
//            val rtUrls: List<Any>? = null,
//            @SerialName("copyright")
//            val copyright: Long? = null,
//            @SerialName("rtype")
//            val rtype: Long? = null,
//            @SerialName("mp3Url")
//            val mp3Url: Any? = null,
//            @SerialName("rurl")
//            val rurl: Any? = null,
//            @SerialName("privilege")
//            val privilege: Privilege? = null,
//            @SerialName("alg")
//            val alg: String? = null

    )

    data class Album(

            @SerialName("title")
            val name: String,
            @SerialName("id")
            val id: Long,
            @SerialName("type")
            val type: String,
            @SerialName("size")
            val size: Long,
            @SerialName("picId")
            val picId: Long? = null,
            @SerialName("blurPicUrl")
            val blurPicUrl: String? = null,
//            @SerialName("companyId")
//            val companyId: Long? = null,
//            @SerialName("pic")
//            val pic: Long? = null,
            @SerialName("picUrl")
            val picUrl: String? = null,
            @SerialName("publishTime")
            val publishTime: Long? = null,
//            @SerialName("description")
//            val description: String? = null,
//            @SerialName("tags")
//            val tags: String? = null,
//            @SerialName("company")
//            val company: Any? = null,
//            @SerialName("briefDesc")
//            val briefDesc: String? = null,
//            @SerialName("artist")
//            val artist: Artist_? = null,
//            @SerialName("songs")
//            val songs: List<Any>? = null,
//            @SerialName("alias")
//            val alias: List<Any>? = null,
//            @SerialName("status")
//            val status: Long? = null,
//            @SerialName("copyrightId")
//            val copyrightId: Long? = null,
//            @SerialName("commentThreadId")
//            val commentThreadId: String? = null,
            @SerialName("artists")
            val artists: List<Artist__>? = null
//            @SerialName("picId_str")
//            val picIdStr: String? = null

    )

    data class Artist(

            @SerialName("title")
            val name: String,
            @SerialName("id")
            val id: Long,

            @SerialName("picUrl")
            val picUrl: String? = null,

            @SerialName("img1v1Url")
            val img1v1Url: String? = null

//            @SerialName("picId")
//            val picId: Long? = null,
//            @SerialName("img1v1Id")
//            val img1v1Id: Long? = null,
//            @SerialName("briefDesc")
//            val briefDesc: String? = null,
//            @SerialName("albumSize")
//            val albumSize: Long? = null,
//            @SerialName("alias")
//            val alias: List<Any>? = null
//            @SerialName("trans")
//            val trans: String? = null,
//            @SerialName("musicSize")
//            val musicSize: Long? = null

    )


    data class Artist__(

            @SerialName("title")
            val name: String? = null,
            @SerialName("id")
            val id: Long? = null,
//            @SerialName("picId")
//            val picId: Long? = null,
//            @SerialName("img1v1Id")
//            val img1v1Id: Long? = null,
//            @SerialName("briefDesc")
//            val briefDesc: String? = null,
            @SerialName("picUrl")
            val picUrl: String? = null,
            @SerialName("img1v1Url")
            val img1v1Url: String? = null
//            @SerialName("albumSize")
//            val albumSize: Long? = null,
//            @SerialName("alias")
//            val alias: List<Any>? = null,
//            @SerialName("trans")
//            val trans: String? = null,
//            @SerialName("musicSize")
//            val musicSize: Long? = null

    )

    data class MusicQuality(
            @SerialName("id")
            val id: Long,
            @SerialName("size")
            val size: Long? = null,
            @SerialName("extension")
            val extension: String? = null,
            @SerialName("sr")
            val sr: Long? = null,
            @SerialName("bitrate")
            val bitrate: Long? = null,
            @SerialName("playTime")
            val playTime: Long? = null,
            @SerialName("volumeDelta")
            val volumeDelta: Double? = null,
            @SerialName("dfsId_str")
            val dfsIdStr: Any? = null

            //            @SerialName("title")
//            val title: String? = null,
//            @SerialName("dfsId")
//            val dfsId: Long? = null,
    )

//    data class Artist_(
//
//            @SerialName("title")
//            val title: String? = null,
//            @SerialName("id")
//            val id: Long? = null,
//            @SerialName("picId")
//            val picId: Long? = null,
//            @SerialName("img1v1Id")
//            val img1v1Id: Long? = null,
//            @SerialName("briefDesc")
//            val briefDesc: String? = null,
//            @SerialName("picUrl")
//            val picUrl: String? = null,
//            @SerialName("img1v1Url")
//            val img1v1Url: String? = null,
//            @SerialName("albumSize")
//            val albumSize: Long? = null,
//            @SerialName("alias")
//            val alias: List<Any>? = null,
//            @SerialName("trans")
//            val trans: String? = null,
//            @SerialName("musicSize")
//            val musicSize: Long? = null
//
//    )

//data class Privilege (
//
//    @SerialName("id")
//    val id: Long? = null,
//    @SerialName("fee")
//    val fee: Long? = null,
//    @SerialName("payed")
//    val payed: Long? = null,
//    @SerialName("st")
//    val st: Long? = null,
//    @SerialName("pl")
//    val pl: Long? = null,
//    @SerialName("dl")
//    val dl: Long? = null,
//    @SerialName("sp")
//    val sp: Long? = null,
//    @SerialName("cp")
//    val cp: Long? = null,
//    @SerialName("subp")
//    val subp: Long? = null,
//    @SerialName("cs")
//    val cs: Boolean? = null,
//    @SerialName("maxbr")
//    val maxbr: Long? = null,
//    @SerialName("fl")
//    val fl: Long? = null,
//    @SerialName("toast")
//    val toast: Boolean? = null,
//    @SerialName("flag")
//    val flag: Long? = null,
//
//)
}

