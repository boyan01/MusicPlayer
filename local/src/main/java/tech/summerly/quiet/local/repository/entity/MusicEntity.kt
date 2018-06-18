package tech.summerly.quiet.local.repository.entity

import android.arch.persistence.room.*
import android.os.Parcel
import android.os.Parcelable
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.model.IAlbum
import tech.summerly.quiet.commonlib.model.IArtist
import tech.summerly.quiet.commonlib.model.IMusic
import tech.summerly.quiet.local.repository.converter.ArchTypeConverter
import tech.summerly.quiet.local.repository.database.LocalMusicDatabase

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
        override val id: Long,
        override val title: String,
        @ColumnInfo(name = "album_id")
        @ForeignKey(entity = AlbumEntity::class, parentColumns = ["id"], childColumns = ["album_id"])
        val albumId: Long,
        val picUri: String?,
        val type: MusicType,
        val mvId: Long,
        override val duration: Long,
        val playUri: String,
        val bitrate: Int
) : IMusic, Parcelable {
    @Ignore
    var _artist: List<IArtist>? = null

    override val artist: List<IArtist>
        get() = _artist ?: LocalMusicDatabase.instance.artistDao().artists(id).also { _artist = it }

    @Ignore
    var _album: IAlbum? = null

    override val album: IAlbum
        get() = _album ?: LocalMusicDatabase.instance.albumDao().album(albumId).also { _album = it }

    override fun getUrl(bitrate: Int): String = playUri

    override val isFavorite: Boolean
        get() = false

    override val artwork: String
        get() = picUri!!

    override suspend fun delete() {
    }

    override suspend fun like() {

    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readLong(),
            source.readString(),
            MusicType.values()[source.readInt()],
            source.readLong(),
            source.readLong(),
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(title)
        writeLong(albumId)
        writeString(picUri)
        writeInt(type.ordinal)
        writeLong(mvId)
        writeLong(duration)
        writeString(playUri)
        writeInt(bitrate)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MusicEntity> = object : Parcelable.Creator<MusicEntity> {
            override fun createFromParcel(source: Parcel): MusicEntity = MusicEntity(source)
            override fun newArray(size: Int): Array<MusicEntity?> = arrayOfNulls(size)
        }
    }
}