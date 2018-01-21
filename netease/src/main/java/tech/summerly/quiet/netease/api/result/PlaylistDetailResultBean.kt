package tech.summerly.quiet.netease.api.result

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/25
 * desc   :
 */
data class PlaylistDetailResultBean(
        val playlist: Playlist?,
        val code: Int) {

    data class Playlist(
            val subscribed: Boolean,
            val tracks: List<Track>?,
            val coverImgUrl: String,
            val trackCount: Int,
            val playCount: Int,
            val trackUpdateTime: Long,
            val updateTime: Long,
            val name: String,
            val id: Long
    )

    data class Track(
            val name: String?,
            val id: Long,
            val ar: List<Artist>?,
            val pop: Int,
            val crbt: String,
            val al: Album?,
            val mv: Long,
            val no: Long,
            val dt: Int
    )

    data class Artist(
            val id: Long,
            val name: String
    )

    data class Album(
            val id: Long,
            val name: String?,
            val picUrl: String?
    )
}