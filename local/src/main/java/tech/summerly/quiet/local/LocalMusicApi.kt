package tech.summerly.quiet.local

import android.content.Context
import tech.summerly.quiet.commonlib.bean.Album
import tech.summerly.quiet.commonlib.bean.Artist
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.local.database.database.LocalMusicDatabase
import tech.summerly.quiet.local.database.entity.MusicArtistRelation
import tech.summerly.quiet.local.utils.EntityMapper

/**
 * Created by summer on 17-12-21
 */
class LocalMusicApi private constructor(context: Context) {
    companion object {

        fun getLocalMusicApi(context: Context) = LocalMusicApi(context.applicationContext)
    }

    private val mapper = EntityMapper()

    private val musicDao = LocalMusicDatabase.getInstance(context).musicDao()

    /**
     * insert a music to database
     */
    fun insertMusic(music: Music) {
        val albumId = insertAlbumSafely(music.album)
        val musicEntity = mapper.convertToMusicEntity(music, albumId)
        val musicId = musicDao.insertMusic(musicEntity)
        val artistIds = insertArtistSafely(music.artist)
        artistIds.map { MusicArtistRelation(musicId, it) }.let { musicDao.insertMusicArtist(it) }
    }

    /**
     * get all musics in local database
     */
    fun getTotalMusics(): List<Music> {
        return musicDao.getTotalMusics().map { musicEntity ->
            val artists = musicDao.getArtistByMusic(musicEntity.id).map(mapper::convertToArtist)
            val album = musicDao.getAlbum(musicEntity.albumId).let(mapper::convertToAlbum)
            mapper.convertToMusic(musicEntity, artists, album)
        }
    }

    /**
     * get all artist in database
     */
    fun getArtists(): List<Artist> {
        return musicDao.getArtists().map(mapper::convertToArtist)
    }

    private fun insertAlbumSafely(album: Album): Long {
        val albumEntity = mapper.convertToAlbumEntity(album)
        val albumId = musicDao.insertAlbum(albumEntity)
        return if (albumId == -1L) {
            musicDao.getAlbumByName(album.name).id
        } else {
            albumId
        }
    }

    /**
     * insert artist to table [tech.summerly.quiet.local.database.entity.ArtistEntity]
     * return the id which artist insert to
     */
    private fun insertArtistSafely(artists: List<Artist>): List<Long> {
        return artists.map(mapper::convertToArtistEntity)
                .map { artistEntity ->
                    val id = musicDao.insertArtist(artistEntity)
                    if (id == -1L) {
                        musicDao.getArtistByName(artistEntity.name).id
                    } else {
                        id
                    }
                }
    }

}