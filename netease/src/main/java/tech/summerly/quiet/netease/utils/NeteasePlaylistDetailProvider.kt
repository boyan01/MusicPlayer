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
internal typealias NeteaseDescription = PlaylistProvider.SimpleDescription

private fun newPlaylistDescrption(playlistBean: PlaylistResultBean.PlaylistBean) = NeteaseDescription(
        playlistBean.id,
        playlistBean.name,
        playlistBean.coverImgUrl,
        playlistBean.subscribed,
        playlistBean.trackCount.toInt(),
        playlistBean.createTime,
        playlistBean.playCount,
        MusicType.NETEASE
)

internal class NeteasePlaylistDetailProvider(
        private val description: NeteaseDescription
) : PlaylistProvider {

    constructor(playlistBean: PlaylistResultBean.PlaylistBean) : this(newPlaylistDescrption(playlistBean))

    override suspend fun getMusicList(): List<Music> {
        require(description.id != 0L)
        val (_, musics) = NeteaseCloudMusicApi().getPlaylistDetail(description.id)
        return musics
    }

    override suspend fun getDescription(): PlaylistProvider.Description? {
        return description
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