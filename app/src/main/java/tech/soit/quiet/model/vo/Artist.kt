package tech.soit.quiet.model.vo

import android.os.Parcelable
import java.io.Serializable

abstract class Artist : Parcelable, Serializable {

    abstract fun getId(): Long

    abstract fun getName(): String

}