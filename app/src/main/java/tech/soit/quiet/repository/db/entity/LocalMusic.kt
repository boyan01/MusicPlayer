package tech.soit.quiet.repository.db.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import tech.soit.quiet.model.local.LocalAlbum
import tech.soit.quiet.model.local.LocalArtist
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music


@Entity(
        tableName = "local_music",
        indices = [
            Index(
                    value = ["fileUri"], unique = true
            ),
            Index(
                    value = ["albumString"], unique = false
            ),
            Index(
                    value = ["artistString"], unique = false
            )]
)
@Parcelize
data class LocalMusic(
        @PrimaryKey(autoGenerate = true)
        private val id: Long,

        /**
         * file path;
         * unique uri, start with file://
         */
        val fileUri: String,

        /**
         * song title
         */
        private val title: String,

        /**
         * song album
         */
        val albumString: String,

        /**
         * artist
         */
        val artistString: String

) : Music() {

    override fun getId(): Long {
        return id
    }

    @Ignore
    override fun getAlbum(): Album {
        return LocalAlbum(albumString, "")
    }

    @Ignore
    override fun getArtists(): List<Artist> {
        return listOf(LocalArtist(artistString))
    }

    override fun getTitle(): String {
        return title
    }

    @Ignore
    override fun getPlayUrl(): String {
        return fileUri
    }

}