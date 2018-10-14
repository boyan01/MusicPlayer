package tech.soit.quiet.model.vo

import tech.soit.quiet.model.po.NeteaseUser

abstract class PlaylistDetail {

    abstract fun getId(): Long

    abstract fun getName(): String

    abstract fun getCoverUrl(): Any

    abstract fun playCount(): Int

    abstract fun getCreator(): NeteaseUser

    abstract fun getTracks(): List<Music>

    abstract fun isSubscribed(): Boolean

}