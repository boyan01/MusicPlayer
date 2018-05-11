package tech.summerly.quiet.commonlib.bean

import android.os.Parcel
import android.os.Parcelable
import tech.summerly.quiet.commonlib.model.IAlbum


data class Album(
        val id: Long,
        val name: String,
        val picUri: String?,
        val type: MusicType
) : Parcelable, IAlbum {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            MusicType.valueOf(parcel.readString()))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(picUri)
        parcel.writeString(type.name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }
}