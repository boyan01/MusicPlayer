package tech.soit.quiet.model.vo

import android.os.Parcelable
import java.io.Serializable

abstract class Music : Parcelable, Serializable {

    abstract fun getId(): Long

    abstract fun getTitle(): String

    abstract fun getAlbum(): Album

    abstract fun getArtists(): List<Artist>

    abstract fun getPlayUrl(): String

}