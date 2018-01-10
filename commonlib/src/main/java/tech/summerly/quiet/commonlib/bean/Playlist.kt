package tech.summerly.quiet.commonlib.bean

/**
 * author : yangbin10
 * date   : 2018/1/9
 */
class Playlist(
        val id: Long,
        val name: String,
        var coverImageUri: String?,
        val musicCount: Int,
        val type: MusicType
) {
    override fun toString(): String {
        return "Playlist(id=$id, name='$name', musicCount=$musicCount)"
    }
}