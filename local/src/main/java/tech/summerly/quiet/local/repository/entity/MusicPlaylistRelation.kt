package tech.summerly.quiet.local.repository.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

/**
 * Created by summer on 17-12-21
 */
@Entity(
        tableName = "relation_music_playlist",
        primaryKeys = ["music_id", "playlist_id"],
        foreignKeys = [
            ForeignKey(entity = MusicEntity::class, parentColumns = ["id"], childColumns = ["music_id"]),
            ForeignKey(entity = PlaylistEntity::class, parentColumns = ["id"], childColumns = ["playlist_id"])
        ]
)
data class MusicPlaylistRelation(
        @ColumnInfo(name = "music_id")
        val musicId: Long,

        @ColumnInfo(name = "playlist_id")
        val playlistId: Long
)