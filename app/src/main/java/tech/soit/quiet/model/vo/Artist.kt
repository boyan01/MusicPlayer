package tech.soit.quiet.model.vo

import android.os.Parcelable

abstract class Artist : Parcelable {

    abstract fun getId(): Long

    abstract fun getName(): String

}