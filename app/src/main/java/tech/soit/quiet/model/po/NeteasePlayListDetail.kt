package tech.soit.quiet.model.po

import com.google.gson.JsonObject
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.model.vo.PlaylistDetail
import tech.soit.quiet.repository.netease.source.NeteaseGlideUrl

class NeteasePlayListDetail(private val jsonObject: JsonObject) : PlaylistDetail() {

    private val tracks: List<Music> = jsonObject["tracks"].asJsonArray.map { playlistTrack(it as JsonObject) }


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

    override fun playCount(): Int {
        return jsonObject["playCount"].asInt
    }

    override fun getCreator(): NeteaseUser {
        val creator = jsonObject["creator"].asJsonObject
        return NeteaseUser(creator["userId"].asLong, creator["nickname"].asString, creator["avatarUrl"].asString)
    }

    override fun getTracks(): List<Music> {
       return tracks
    }

    private fun playlistTrack(jsonObject: JsonObject): Music {
        return Music(
                jsonObject["id"].asLong,
                jsonObject["name"].asString,
                Album(jsonObject["al"].asJsonObject["name"].asString),
                jsonObject["ar"].asJsonArray.map {
                    it as JsonObject
                    Artist(it["name"].asString)
                },
                mapOf(Music.URI to "TODO", Music.PIC_URI to jsonObject["al"].asJsonObject["picUrl"].asString))
    }

}