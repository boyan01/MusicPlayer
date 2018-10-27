package tech.soit.quiet.model.po

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.android.parcel.Parcelize
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.utils.string

@Parcelize
class NeteaseArtist(
        private val id: Long,
        private val name: String
) : Artist() {

    companion object {


        /**
         * remove json ar json object
         */
        fun fromJson(ar: JsonArray?): List<NeteaseArtist> {
            ar ?: return emptyList()
            return ar.map { e ->
                e as JsonObject
                NeteaseArtist(e["id"].asLong, e["name"].string)
            }
        }

    }


    override fun getId(): Long {
        return id
    }

    override fun getName(): String {
        return name
    }
}