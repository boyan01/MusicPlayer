package tech.summerly.quiet.local.repository.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

/**
 * Created by summer on 17-12-21
 */
@Entity(
        tableName = "relation_music_artist",
        primaryKeys = ["music_id", "artist_id"],
        foreignKeys = [
            ForeignKey(entity = MusicEntity::class, parentColumns = ["id"], childColumns = ["music_id"]),
            ForeignKey(entity = ArtistEntity::class, parentColumns = ["id"], childColumns = ["artist_id"])
        ]
)
data class MusicArtistRelation(
        @ColumnInfo(name = "music_id")
        val musicId: Long,
        @ColumnInfo(name = "artist_id")
        val artistId: Long
)