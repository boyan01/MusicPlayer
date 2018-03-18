package tech.summerly.quiet.netease.utils

import android.os.Parcel
import android.os.Parcelable
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.model.PlaylistProvider
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi
import tech.summerly.quiet.service.netease.result.PlaylistResultBean

/**
 * Created by summer on 18-3-18
 */
internal class NeteasePlaylistDetailProvider(
        private val description: NeteaseDescription
) : PlaylistProvider {

    constructor(playlistBean: PlaylistResultBean.PlaylistBean) : this(NeteaseDescription(playlistBean))

    override suspend fun getMusicList(): List<Music> {
        require(description.id != 0L)
        val (_, musics) = NeteaseCloudMusicApi().getPlaylistDetail(description.id)
        return musics
    }

    override suspend fun getDescription(): PlaylistProvider.Description? {
        return description
    }


    internal class NeteaseDescription(
            override val id: Long,
            override val name: String,
            override val coverImgUrl: String,
            override val subscribed: Boolean,
            override val trackCount: Int,
            override val createTime: Long,
            override val playCount: Long,
            override val type: MusicType) : PlaylistProvider.Description, Parcelable {

        constructor(playlistBean: PlaylistResultBean.PlaylistBean) : this(
                playlistBean.id,
                playlistBean.name,
                playlistBean.coverImgUrl,
                playlistBean.subscribed,
                playlistBean.trackCount.toInt(),
                playlistBean.createTime,
                playlistBean.playCount,
                MusicType.NETEASE
        )

        constructor(source: Parcel) : this(
                source.readLong(),
                source.readString(),
                source.readString(),
                1 == source.readInt(),
                source.readInt(),
                source.readLong(),
                source.readLong(),
                MusicType.values()[source.readInt()]
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
            writeLong(id)
            writeString(name)
            writeString(coverImgUrl)
            writeInt((if (subscribed) 1 else 0))
            writeInt(trackCount)
            writeLong(createTime)
            writeLong(playCount)
            writeInt(type.ordinal)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<NeteaseDescription> = object : Parcelable.Creator<NeteaseDescription> {
                override fun createFromParcel(source: Parcel): NeteaseDescription = NeteaseDescription(source)
                override fun newArray(size: Int): Array<NeteaseDescription?> = arrayOfNulls(size)
            }
        }
    }

    constructor(source: Parcel) : this(
            source.readParcelable<NeteaseDescription>(NeteaseDescription::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(description, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<NeteasePlaylistDetailProvider> = object : Parcelable.Creator<NeteasePlaylistDetailProvider> {
            override fun createFromParcel(source: Parcel): NeteasePlaylistDetailProvider = NeteasePlaylistDetailProvider(source)
            override fun newArray(size: Int): Array<NeteasePlaylistDetailProvider?> = arrayOfNulls(size)
        }
    }
}