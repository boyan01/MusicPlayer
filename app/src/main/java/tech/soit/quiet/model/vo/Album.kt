package tech.soit.quiet.model.vo

import android.os.Parcelable
import java.io.Serializable

abstract class Album : Parcelable, Serializable {


    abstract fun getCoverImageUrl(): Any?

    abstract fun getName(): String

    abstract fun getId(): Long

}