package tech.summerly.quiet.service.local.database.entity

import android.arch.persistence.room.*
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.service.local.database.converter.ArchTypeConverter

/**
 * Created by summer on 17-12-21
 */
@Entity(
        tableName = "entity_music",
        indices = [Index(value = ["playUri"], unique = true)]
)
@TypeConverters(ArchTypeConverter::class)
data class MusicEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val title: String,
        @ColumnInfo(name = "album_id")
        @ForeignKey(entity = AlbumEntity::class, parentColumns = ["id"], childColumns = ["album_id"])
        val albumId: Long,
        val picUri: String?,
        val type: MusicType,
        val mvId: Long,
        val duration: Long,
        val playUri: String,
        val bitrate: Int
)