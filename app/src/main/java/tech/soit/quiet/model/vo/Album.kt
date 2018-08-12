package tech.soit.quiet.model.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
        val title: String
) : Parcelable