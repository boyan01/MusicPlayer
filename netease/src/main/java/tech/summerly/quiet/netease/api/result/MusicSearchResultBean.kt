package tech.summerly.quiet.netease.api.result

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/23
 * desc   :
 */
data class MusicSearchResultBean(val result: ResultBean,
                                 val code: Int) {
    data class ResultBean(val songs: List<SongsBean>?,
                          val songCount: Int)

    data class SongsBean(val id: Long,
                         val name: String,
                         val artists: List<ArtistsBean>?,
                         val album: AlbumBean?,
                         val duration: Long,
                         val copyrightId: Long,
                         val status: Long,
                         val alias: List<Any?>,
                         val rtype: Long,
                         val ftype: Long,
                         val mvid: Long,
                         val fee: Long,
                         var rUrl: Any?)


    data class ArtistsBean(val id: Long,
                           val name: String,
                           var picUrl: String?,
                           val alias: List<Any?>,
                           val albumSize: Long,
                           val picId: Long,
                           val img1v1Url: String,
                           val img1v1: Long,
                           var trans: Any?)

    data class AlbumBean(val id: Long,
                         val name: String,
                         val artist: ArtistBean,
                         val publishTime: Long,
                         val size: Long,
                         val copyrightId: Long,
                         val status: Long,
                         val picId: Long)

    data class ArtistBean(val id: Long,
                          val name: String,
                          var picUrl: Any?,
                          val alias: List<Any?>,
                          val albumSize: Long,
                          val picId: Long,
                          val img1v1Url: String,
                          val img1v1: Long,
                          var trans: Any?)
}

