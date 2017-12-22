package tech.summerly.quiet.commonlib.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class MusicUri(
        val bitrate: Int,

        val uri: String,

        /**
         * Based on this variable, the player determines if the song is playable.
         *
         * When [dateValid] is greater than the currentTimeMillis, you can think
         * of this link [uri] has been invalidated
         */
        val dateValid: Long
) : Parcelable {

    fun isValid() = dateValid > System.currentTimeMillis()
}