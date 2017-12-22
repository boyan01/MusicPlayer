package tech.summerly.quiet.commonlib.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@SuppressLint("ParcelCreator")
@Parcelize
data class Artist(
        val id: Long,
        val name: String,
        val picUri: String?,
        val type: MusicType
) : Parcelable {
    companion object {
        fun fromString(artist: String, type: MusicType, picUri: String?): List<Artist> {
            return artist.split('/').map {
                Artist(
                        id = 0,
                        name = it,
                        picUri = picUri,
                        type = type
                )
            }
        }
    }
}