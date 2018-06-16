package tech.summerly.quiet.local.utils

import android.os.Parcel
import android.os.Parcelable
import tech.summerly.quiet.commonlib.bean.Album
import tech.summerly.quiet.commonlib.bean.Artist
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicType
import tech.summerly.quiet.commonlib.model.PlaylistProvider
import tech.summerly.quiet.local.repository.LocalMusicApi

/**
 * 本地专辑详情
 */

private fun newAlbumDescrpition(album: Album) = PlaylistProvider.SimpleDescription(
        album.id,
        album.name,
        album.picUri ?: "",
        false,
        0,
        0,
        0,
        MusicType.LOCAL
)

internal class AlbumDetailProvider(
        private val album: Album
) : PlaylistProvider, Parcelable {

    override suspend fun getMusicList(): List<Music> {
        return LocalMusicApi.getLocalMusicApi().getMusicsByAlbum(album).await()
    }

    override suspend fun getDescription(): PlaylistProvider.Description? {
        return newAlbumDescrpition(album)
    }

    constructor(source: Parcel) : this(
            source.readParcelable<Album>(Album::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(album, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AlbumDetailProvider> = object : Parcelable.Creator<AlbumDetailProvider> {
            override fun createFromParcel(source: Parcel): AlbumDetailProvider = AlbumDetailProvider(source)
            override fun newArray(size: Int): Array<AlbumDetailProvider?> = arrayOfNulls(size)
        }
    }
}


internal class ArtistDetailProvider(
        private val artist: Artist
) : PlaylistProvider {

    override suspend fun getMusicList(): List<Music> {
        return LocalMusicApi.getLocalMusicApi().getMusicsByArtist(artist)
    }

    override suspend fun getDescription(): PlaylistProvider.Description? {
        return null
    }

    override val title: String
        get() = artist.name

    constructor(source: Parcel) : this(
            source.readParcelable<Artist>(Artist::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeParcelable(artist, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ArtistDetailProvider> = object : Parcelable.Creator<ArtistDetailProvider> {
            override fun createFromParcel(source: Parcel): ArtistDetailProvider = ArtistDetailProvider(source)
            override fun newArray(size: Int): Array<ArtistDetailProvider?> = arrayOfNulls(size)
        }
    }
}