package tech.soit.quiet.model.po

import com.google.gson.JsonObject
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.model.vo.User
import tech.soit.quiet.repository.netease.source.NeteaseGlideUrl
import tech.soit.quiet.utils.component.support.value

class NeteasePlayListDetail(jsonObject: JsonObject) : PlayListDetail() {

    private val tracks: List<Music>

    private val id: Long = jsonObject["id"].asLong

    private val name: String = jsonObject["name"].asString

    private val coverUrl: String = jsonObject["coverImgUrl"].asString

    private val creator: User

    private val isSubscribed: Boolean = jsonObject["subscribed"].value() ?: false

    private val playCount: Int = jsonObject["playCount"].asInt

    private val trackCount: Int = jsonObject["trackCount"].asInt

    init {
        val trackJson = jsonObject["tracks"]
        tracks = if (trackJson == null || trackJson.isJsonNull) {
            NONE_TRACKS
        } else {
            try {
                trackJson.asJsonArray.map { playlistTrack(it as JsonObject) }
            } catch (e: Exception) {
                NONE_TRACKS
            }
        }

        val c = jsonObject["creator"].asJsonObject
        creator = NeteaseUser(c["userId"].asLong, c["nickname"].asString, c["avatarUrl"].asString)
    }

    override fun getId(): Long {
        return id
    }

    override fun getName(): String {
        return name
    }

    override
    fun getCoverUrl(): Any {
        return NeteaseGlideUrl(coverUrl)
    }

    override fun getCreator(): User {
        return creator
    }

    override fun getTracks(): List<Music> {
        return tracks
    }

    override fun isSubscribed(): Boolean {
        return isSubscribed
    }

    override fun getPlayCount(): Int {
        return playCount
    }

    override fun getTrackCount(): Int {
        return trackCount
    }

    /**
     * create music from playlist track json
     */
    private fun playlistTrack(jsonObject: JsonObject): NeteaseMusic {

        val album = NeteaseAlbum.fromJson(jsonObject["al"].asJsonObject)
        val artist = NeteaseArtist.fromJson(jsonObject["ar"].asJsonArray)

        return NeteaseMusic(
                jsonObject["id"].asLong,
                jsonObject["name"].asString,
                album,
                artist
        )
    }

}