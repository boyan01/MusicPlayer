package tech.soit.quiet.model.vo

import android.os.Parcelable

abstract class Music : Parcelable {

    abstract fun getId(): Long

    abstract fun getTitle(): String

    abstract fun getAlbum(): Album

    abstract fun getArtists(): List<Artist>

    abstract fun getPlayUrl(): String

}