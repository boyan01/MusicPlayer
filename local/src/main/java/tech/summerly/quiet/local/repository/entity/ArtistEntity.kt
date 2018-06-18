package tech.summerly.quiet.local.repository.entity

import android.arch.persistence.room.*
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.model.IArtist
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
) : IArtist {


    @Ignore
    override fun name(): String {
        return name
    }
}