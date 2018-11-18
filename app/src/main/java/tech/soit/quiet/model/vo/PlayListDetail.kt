package tech.soit.quiet.model.vo

import java.io.Serializable

abstract class PlayListDetail : Serializable {

    companion object {

        val NONE_TRACKS: List<Music> = ArrayList()

    }

    abstract fun getId(): Long

    abstract fun getName(): String

    abstract fun getCoverUrl(): Any

    abstract fun getCreator(): User

    abstract fun getTracks(): List<Music>

    abstract fun isSubscribed(): Boolean

    abstract fun getPlayCount(): Int

    abstract fun getTrackCount(): Int

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PlayListDetail
        return getId() == other.getId()
    }

    override fun hashCode(): Int {
        return getId().hashCode()
    }

    fun getToken(): String {
        return javaClass.simpleName + "-" + getId()
    }


}