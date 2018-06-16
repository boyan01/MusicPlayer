package tech.summerly.quiet.local.repository.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.local.repository.converter.ArchTypeConverter

/**
 * Created by summer on 17-12-21
 */
@Entity(
        tableName = "entity_artist",
        indices = [Index(value = ["name"], unique = true)]
)
@TypeConverters(ArchTypeConverter::class)
data class ArtistEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val name: String,
        val picUri: String?,
        val type: MusicType
)