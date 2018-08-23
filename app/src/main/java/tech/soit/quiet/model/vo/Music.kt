package tech.soit.quiet.model.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Music(
        val id: Long,
        val title: String,
        val album: Album,
        val artists: List<Artist>,
        //附加
        val attach: Map<String, String> = emptyMap()
) : Parcelable {

    companion object {

        const val URI = "uri"

    }

}