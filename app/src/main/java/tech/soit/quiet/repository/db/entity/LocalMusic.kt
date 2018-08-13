package tech.soit.quiet.repository.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import tech.soit.quiet.model.vo.Album
import tech.soit.quiet.model.vo.Artist
import tech.soit.quiet.model.vo.Music


@Entity(
        tableName = "local_music",
        indices = [
            Index(
                    value = ["uri"], unique = true
            ),
            Index(
                    value = ["album"], unique = false
            ),
            Index(
                    value = ["artist"], unique = false
            )]
)
data class LocalMusic(
        @PrimaryKey(autoGenerate = true)
        val id: Long,

        /**
         * file path;
         * unique uri, start with file://
         */
        val uri: String,

        /**
         * song title
         */
        val title: String,

        /**
         * song album
         */
        val album: String,

        /**
         * artist
         */
        val artist: String

) {

    fun toMusic(): Music {
        return Music(id, title, Album(album), listOf(Artist(artist)), mapOf("uri" to uri))
    }

}