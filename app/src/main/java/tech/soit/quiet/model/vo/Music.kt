package tech.soit.quiet.model.vo

import android.os.Parcelable
import java.io.Serializable

abstract class Music : Parcelable, Serializable {

    abstract fun getId(): Long

    abstract fun getTitle(): String

    abstract fun getAlbum(): Album

    abstract fun getArtists(): List<Artist>

    abstract fun getPlayUrl(): String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Music

        if (getId() != other.getId()) return false

        return true
    }

    override fun hashCode(): Int {
        return getId().hashCode()
    }


}