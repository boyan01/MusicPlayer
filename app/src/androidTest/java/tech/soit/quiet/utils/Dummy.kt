package tech.soit.quiet.utils

import kotlinx.android.parcel.Parcelize
import tech.soit.quiet.model.local.LocalAlbum
import tech.soit.quiet.model.local.LocalArtist
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music
import tech.soit.quiet.player.core.QuietMediaPlayerTest
import tech.soit.quiet.player.playlist.Playlist

/**
 * provider dummy data
 */
object Dummy {

    @Parcelize
    data class DummyMusic(
            private val id: Long,
            private val title: String,
            private val album: String,
            private val artist: List<String>
    ) : Music() {

        override fun getId(): Long {
            return id
        }

        override fun getTitle(): String {
            return title
        }

        override fun getAlbum(): Album {
            return LocalAlbum(album, "https://via.placeholder.com/350x150")
        }

        override fun getArtists(): List<Artist> {
            return artist.map { LocalArtist(it) }
        }

        override fun getPlayUrl(): String {
            return QuietMediaPlayerTest.URI
        }
    }


    val MUSICS: List<DummyMusic> = listOf(
            DummyMusic(1, "test1", "album1", listOf("artist1", "artist2")),
            DummyMusic(2, "test2", "album2", listOf("artist2")),
            DummyMusic(3, "test3", "album1", listOf("artist1")),
            DummyMusic(4, "test4", "album2", listOf("artist2", "artist3")),
            DummyMusic(5, "test5", "album1", listOf("artist3"))
    )




    val PLAYLIST = Playlist("test", MUSICS)

}