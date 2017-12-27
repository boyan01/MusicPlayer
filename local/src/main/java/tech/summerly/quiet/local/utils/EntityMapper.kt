package tech.summerly.quiet.local.utils

import tech.summerly.quiet.commonlib.bean.Album
import tech.summerly.quiet.commonlib.bean.Artist
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.MusicUri
import tech.summerly.quiet.local.database.entity.AlbumEntity
import tech.summerly.quiet.local.database.entity.ArtistEntity
import tech.summerly.quiet.local.database.entity.MusicEntity

/**
 * Created by summer on 17-12-22
 *
 * mapper of java bean and table entity
 */
class EntityMapper {

    fun convertToMusicEntity(music: Music, albumId: Long = music.album.id): MusicEntity = with(music) {
        MusicEntity(
                id = id,
                title = title,
                albumId = albumId,
                picUri = picUri,
                type = type,
                mvId = mvId,
                duration = duration,
                playUri = playUri.getOrNull(0)?.uri ?: "",
                bitrate = playUri.getOrNull(0)?.bitrate ?: 0
        )
    }

    fun convertToArtistEntity(artist: Artist): ArtistEntity = with(artist) {
        ArtistEntity(id, name, picUri, type)
    }

    fun convertToAlbumEntity(album: Album): AlbumEntity = with(album) {
        AlbumEntity(id, name, picUri, type)
    }

    fun convertToAlbum(albumEntity: AlbumEntity): Album = with(albumEntity) {
        Album(id, name, picUri, type)
    }

    fun convertToArtist(artistEntity: ArtistEntity): Artist = with(artistEntity) {
        Artist(id, name, picUri, type)
    }

    fun convertToMusic(musicEntity: MusicEntity, artists: List<Artist>, album: Album): Music = with(musicEntity) {
        Music(
                id = id,
                title = title,
                album = album,
                artist = artists,
                playUri = listOf(MusicUri(bitrate, playUri, Long.MAX_VALUE)),
                type = type,
                duration = duration,
                picUri = picUri,
                mvId = 0
        )
    }
}