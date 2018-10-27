package tech.soit.quiet.model.vo

abstract class PlayListDetail {

    abstract fun getId(): Long

    abstract fun getName(): String

    abstract fun getCoverUrl(): Any

    abstract fun getCreator(): User

    abstract fun getTracks(): List<Music>

    abstract fun isSubscribed(): Boolean

    abstract fun getPlayCount(): Int

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PlayListDetail
        return getId() == other.getId()
    }

    override fun hashCode(): Int {
        return getId().hashCode()
    }


}