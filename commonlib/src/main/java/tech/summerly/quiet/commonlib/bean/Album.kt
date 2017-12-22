package tech.summerly.quiet.commonlib.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class Album(
        val id: Long,
        val name: String,
        val picUri: String?,
        val type: MusicType
) : Parcelable