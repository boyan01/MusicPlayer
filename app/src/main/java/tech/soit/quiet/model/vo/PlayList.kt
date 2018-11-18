package tech.soit.quiet.model.vo

/**
 * 歌单
 */
@Deprecated("user PlayListDetail")
abstract class PlayList {

    abstract fun getId(): Long

    abstract fun getName(): String

    abstract fun getCoverImageUrl(): Any

    abstract fun getDescription(): String

    abstract fun getTrackCount(): Int

    abstract fun getUserId(): Long

}