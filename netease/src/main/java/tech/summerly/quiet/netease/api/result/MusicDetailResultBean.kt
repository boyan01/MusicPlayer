package tech.summerly.quiet.netease.api.result


/**
 * author : SUMMERLY
 * time   : 2017/8/23
 * desc   : 得到的结果
 * 注释掉的参数是 json 中原有的,但是我们并不感兴趣的参数.
 */
data class MusicDetailResultBean(
        var songs: List<Song>,

//        var privileges: List<Privilege>,

        var code: Int
) {
    //音乐详情
    data class Song(

            val title: String,

            val id: Long,

            var ar: List<Artist>? = null, //艺术家

            var alia: List<Any>? = null, //别名

            val pop: Float, //流行指数

            var al: Album? = null,


            //歌曲质量
            var h: Quality? = null,
            var m: Quality? = null,
            var l: Quality? = null,

            val mv: Long? = null//mv的ID

//        var pst: Long? = null,
//        var t: Long? = null,


//        var st: Long? = null,
//        var rt: String? = null,
//        var fee: Long? = null,
//        var v: Long? = null,
//        var crbt: Any? = null,
//        var cf: String? = null,


//        var dt: Long? = null,


//        var a: Any? = null,
//        var cd: String? = null,
//        var no: Long? = null,
//        var rtUrl: Any? = null,
//        var ftype: Long? = null,
//        var rtUrls: List<Any>? = null,
//        var djId: Long? = null,

//        var copyright: Long? = null,
//
//        var sId: Long? = null,
//        var rtype: Long? = null,
//        var rurl: Any? = null,
//        var mst: Long? = null,
//        var cp: Long? = null,

    )


    data class Album(
            var id: Long,
            var title: String,
            var picUrl: String? = null,
            var pic: Long? = null,
            var tns: List<String>? = null//翻译
    )

    data class Artist(
            var id: Long? = null,
            var title: String? = null
    )

    data class Quality(

            var br: Long,

            var size: Long,

            var vd: Float? = null

    )


//class Privilege {
//
//    var id: Long? = null
//    var fee: Long? = null
//    var payed: Long? = null
//    var st: Long? = null
//    var pl: Long? = null
//    var dl: Long? = null
//    var sp: Long? = null
//    var cp: Long? = null
//    var subp: Long? = null
//    var cs: Boolean? = null
//    var maxbr: Long? = null
//    var fl: Long? = null
//    var toast: Boolean? = null
//    var flag: Long? = null
//
//}
}