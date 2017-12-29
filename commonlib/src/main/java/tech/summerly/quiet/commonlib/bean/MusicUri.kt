package tech.summerly.quiet.commonlib.bean

import android.os.Parcel
import android.os.Parcelable

/**
 *
 */
data class MusicUri(
        val bitrate: Int,

        val uri: String,

        /**
         * Based on this variable, the player determines if the song is playable.
         *
         * When [dateValid] is greater than the currentTimeMillis, you can think
         * of this link [uri] has been invalidated
         */
        val dateValid: Long,
        val md5: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString())

    fun isValid() = dateValid > System.currentTimeMillis()
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bitrate)
        parcel.writeString(uri)
        parcel.writeLong(dateValid)
        parcel.writeString(md5)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MusicUri> {
        override fun createFromParcel(parcel: Parcel): MusicUri {
            return MusicUri(parcel)
        }

        override fun newArray(size: Int): Array<MusicUri?> {
            return arrayOfNulls(size)
        }
    }
}