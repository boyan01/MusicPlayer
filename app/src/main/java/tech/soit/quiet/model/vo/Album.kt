package tech.soit.quiet.model.vo

import android.os.Parcelable

abstract class Album : Parcelable {


    abstract fun getCoverImageUrl(): Any?

    abstract fun getName(): String

    abstract fun getId(): Long

}