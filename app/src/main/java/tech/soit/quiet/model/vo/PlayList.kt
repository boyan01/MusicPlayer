package tech.soit.quiet.model.vo

/**
 * 歌单
 */
abstract class PlayList {

    abstract fun getDescription(): String

    abstract fun getCoverImageUrl(): Any

    abstract fun getTrackCount(): Int

    abstract fun getName(): String

    abstract fun getId(): Long

    abstract fun getUserId(): Long

}