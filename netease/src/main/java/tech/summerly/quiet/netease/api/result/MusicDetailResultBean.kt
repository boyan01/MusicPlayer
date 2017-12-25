package tech.summerly.quiet.netease.api.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/23
 * desc   : 得到的结果
 * 注释掉的参数是 json 中原有的,但是我们并不感兴趣的参数.
 */
data class MusicDetailResultBean(
        @SerializedName("songs")
        @Expose
        var songs: List<Song>,

        //        @SerializedName("privileges")
//        @Expose
//        var privileges: List<Privilege>,

        @SerializedName("code")
        @Expose
        var code: Int
) {
    //音乐详情
    data class Song(

            @SerializedName("title")
            @Expose
            val name: String,

            @SerializedName("id")
            @Expose
            val id: Long,

            @SerializedName("ar")
            @Expose
            var artist: List<Artist>? = null, //艺术家

            @SerializedName("alia")
            @Expose
            var alia: List<Any>? = null, //别名

            @SerializedName("pop")
            @Expose
            val pop: Float, //流行指数

            @SerializedName("al")
            @Expose
            var album: Album? = null,


            //歌曲质量
            @SerializedName("h")
            @Expose
            var high: Quality? = null,
            @SerializedName("m")
            @Expose
            var medium: Quality? = null,
            @SerializedName("l")
            @Expose
            var low: Quality? = null,

            @SerializedName("mv")
            @Expose
            val mv: Long? = null//mv的ID

            //        @SerializedName("pst")
//        @Expose
//        var pst: Long? = null,
//        @SerializedName("t")
//        @Expose
//        var t: Long? = null,


//        @SerializedName("st")
//        @Expose
//        var st: Long? = null,
//        @SerializedName("rt")
//        @Expose
//        var rt: String? = null,
//        @SerializedName("fee")
//        @Expose
//        var fee: Long? = null,
//        @SerializedName("v")
//        @Expose
//        var v: Long? = null,
//        @SerializedName("crbt")
//        @Expose
//        var crbt: Any? = null,
//        @SerializedName("cf")
//        @Expose
//        var cf: String? = null,


            //        @SerializedName("dt")
//        @Expose
//        var dt: Long? = null,


            //        @SerializedName("a")
//        @Expose
//        var a: Any? = null,
//        @SerializedName("cd")
//        @Expose
//        var cd: String? = null,
//        @SerializedName("no")
//        @Expose
//        var no: Long? = null,
//        @SerializedName("rtUrl")
//        @Expose
//        var rtUrl: Any? = null,
//        @SerializedName("ftype")
//        @Expose
//        var ftype: Long? = null,
//        @SerializedName("rtUrls")
//        @Expose
//        var rtUrls: List<Any>? = null,
//        @SerializedName("djId")
//        @Expose
//        var djId: Long? = null,

//        @SerializedName("copyright")
//        @Expose
//        var copyright: Long? = null,
//
//        @SerializedName("s_id")
//        @Expose
//        var sId: Long? = null,
//        @SerializedName("rtype")
//        @Expose
//        var rtype: Long? = null,
//        @SerializedName("rurl")
//        @Expose
//        var rurl: Any? = null,
//        @SerializedName("mst")
//        @Expose
//        var mst: Long? = null,
//        @SerializedName("cp")
//        @Expose
//        var cp: Long? = null,

    )


    data class Album(

            @SerializedName("id")
            @Expose
            var id: Long,

            @SerializedName("title")
            @Expose
            var name: String,

            @SerializedName("picUrl")
            @Expose
            var picUrl: String? = null,

            @SerializedName("pic")
            @Expose
            var pic: Long? = null,

            @SerializedName("tns")
            @Expose
            var translation: List<String>? = null//翻译
    )

    data class Artist(
            @SerializedName("id")
            @Expose
            var id: Long? = null,

            @SerializedName("title")
            @Expose
            var name: String? = null

//        @SerializedName("tns")
//        @Expose
//        var tns: List<Any>? = null,
//
//        @SerializedName("alias")
//        @Expose
//        var alias: List<Any>? = null
    )

    data class Quality(

            @SerializedName("br")
            @Expose
            var bitRate: Long,

            @SerializedName("size")
            @Expose
            var size: Long,

            @SerializedName("vd")
            @Expose
            var vd: Float? = null

            //    @SerializedName("fid")
//    @Expose
//    var fid: Long? = null,

    )


//class Privilege {
//
//    @SerializedName("id")
//    @Expose
//    var id: Long? = null
//    @SerializedName("fee")
//    @Expose
//    var fee: Long? = null
//    @SerializedName("payed")
//    @Expose
//    var payed: Long? = null
//    @SerializedName("st")
//    @Expose
//    var st: Long? = null
//    @SerializedName("pl")
//    @Expose
//    var pl: Long? = null
//    @SerializedName("dl")
//    @Expose
//    var dl: Long? = null
//    @SerializedName("sp")
//    @Expose
//    var sp: Long? = null
//    @SerializedName("cp")
//    @Expose
//    var cp: Long? = null
//    @SerializedName("subp")
//    @Expose
//    var subp: Long? = null
//    @SerializedName("cs")
//    @Expose
//    var cs: Boolean? = null
//    @SerializedName("maxbr")
//    @Expose
//    var maxbr: Long? = null
//    @SerializedName("fl")
//    @Expose
//    var fl: Long? = null
//    @SerializedName("toast")
//    @Expose
//    var toast: Boolean? = null
//    @SerializedName("flag")
//    @Expose
//    var flag: Long? = null
//
//}
}