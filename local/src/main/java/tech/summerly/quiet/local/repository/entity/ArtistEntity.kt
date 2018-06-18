package tech.summerly.quiet.local.repository.entity

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
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
) : IArtist, Parcelable {
    @Ignore
    override fun name(): String {
        return name
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readString(),
            MusicType.values()[source.readInt()]
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(name)
        writeString(picUri)
        writeInt(type.ordinal)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArtistEntity> = object : Parcelable.Creator<ArtistEntity> {
            override fun createFromParcel(source: Parcel): ArtistEntity = ArtistEntity(source)
            override fun newArray(size: Int): Array<ArtistEntity?> = arrayOfNulls(size)
        }
    }
}