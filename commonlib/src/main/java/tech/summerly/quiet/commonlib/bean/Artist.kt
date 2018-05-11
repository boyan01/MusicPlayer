package tech.summerly.quiet.commonlib.bean

import android.os.Parcel
import android.os.Parcelable
import tech.summerly.quiet.commonlib.model.IAlbum
import tech.summerly.quiet.commonlib.model.IArtist


data class Artist(
        val id: Long,
        val name: String,
        val picUri: String?,
        val type: MusicType
) : Parcelable, IArtist {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            MusicType.valueOf(parcel.readString()))

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

        @JvmField
        val CREATOR = object : Parcelable.Creator<Artist> {
            override fun createFromParcel(parcel: Parcel): Artist {
                return Artist(parcel)
            }

            override fun newArray(size: Int): Array<Artist?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(picUri)
        parcel.writeString(type.name)
    }

    override fun describeContents(): Int {
        return 0
    }


}