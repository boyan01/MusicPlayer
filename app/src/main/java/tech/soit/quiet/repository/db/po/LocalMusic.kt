package tech.soit.quiet.repository.db.po

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


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

)