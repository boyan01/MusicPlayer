package tech.soit.quiet.model.po

import com.google.gson.JsonObject
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.repository.netease.source.NeteaseGlideUrl

class NeteasePlayListDetail(private val jsonObject: JsonObject) : PlayListDetail() {

    private val tracks: List<NeteaseMusic> = jsonObject["tracks"].asJsonArray.map { playlistTrack(it as JsonObject) }


    override fun getId(): Long {
        return jsonObject["id"].asLong
    }

    override fun getName(): String {
        return jsonObject["name"].asString
    }

    override
    fun getCoverUrl(): Any {
        return NeteaseGlideUrl(jsonObject["coverImgUrl"].asString)
    }

    override fun getCreator(): NeteaseUser {
        val creator = jsonObject["creator"].asJsonObject
        return NeteaseUser(creator["userId"].asLong, creator["nickname"].asString, creator["avatarUrl"].asString)
    }

    override fun getTracks(): List<Music> {
        return tracks
    }

    override fun isSubscribed(): Boolean {
        return jsonObject["subscribed"].asBoolean
    }

    override fun getPlayCount(): Int {
        return jsonObject["playCount"].asInt
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