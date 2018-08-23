package tech.soit.quiet.model.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Artist(
        val name: String
) : Parcelable