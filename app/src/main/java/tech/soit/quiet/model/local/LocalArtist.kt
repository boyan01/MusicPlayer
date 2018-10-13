package tech.soit.quiet.model.local

import kotlinx.android.parcel.Parcelize
import tech.soit.quiet.model.vo.Artist

@Parcelize
class LocalArtist(
        private val name: String
) : Artist() {

    override fun getId(): Long {
        return 0 /* local artist do not have id yet */
    }

    override fun getName(): String {
        return name
    }
}