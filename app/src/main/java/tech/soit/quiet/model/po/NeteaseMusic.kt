package tech.soit.quiet.model.po

import kotlinx.android.parcel.Parcelize
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music

@Parcelize
class NeteaseMusic(
        private val id: Long,
        private val title: String,
        private val album: NeteaseAlbum,
        private val artists: List<Artist>
) : Music() {

    override fun getId(): Long {
        return id
    }

    override fun getTitle(): String {
        return title
    }

    override fun getAlbum(): Album {
        return album
    }

    override fun getArtists(): List<Artist> {
        return artists
    }

    override fun getPlayUrl(): String {
        return "http://music.163.com/song/media/outer/url?id=$id.mp3"
    }


}