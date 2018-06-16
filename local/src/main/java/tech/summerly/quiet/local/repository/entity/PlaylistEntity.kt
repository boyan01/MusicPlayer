package tech.summerly.quiet.local.repository.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by summer on 17-12-21
 */
@Entity(
        tableName = "entity_playlist"
)
data class PlaylistEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val title: String,
        val coverUri: String?
)