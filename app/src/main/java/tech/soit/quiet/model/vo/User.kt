package tech.soit.quiet.model.vo

import java.io.Serializable

abstract class User : Serializable {

    abstract fun getId(): Long

    abstract fun getNickName(): String

    abstract fun getAvatarUrl(): Any

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as User
        return getId() == other.getId()
    }

    override fun hashCode(): Int {
        return getId().hashCode()
    }


}