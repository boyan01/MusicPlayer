package tech.summerly.quiet.local.repository

import android.content.Context
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.async
import tech.summerly.quiet.commonlib.bean.Album
import tech.summerly.quiet.commonlib.bean.Artist
import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.bean.Playlist
import tech.summerly.quiet.commonlib.utils.inTransaction
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.repository.converter.EntityMapper
import tech.summerly.quiet.local.repository.database.LocalMusicDatabase
import tech.summerly.quiet.local.repository.database.Table
import tech.summerly.quiet.local.repository.entity.MusicArtistRelation
import tech.summerly.quiet.local.repository.entity.MusicPlaylistRelation
import tech.summerly.quiet.local.repository.entity.PlaylistEntity
import java.io.File

/**
 * Created by summer on 17-12-21
 */
class LocalMusicApi private constructor(context: Context) {

    companion object {
        fun getLocalMusicApi(context: Context = LocalModule) = LocalMusicApi(context.applicationContext)
        val instance by lazy { LocalMusicApi(LocalModule) }
    }

    private val mapper = EntityMapper()

    private val database = LocalMusicDatabase.getInstance(context)

    private val musicDao = database.musicDao()

    /**
     * insert a music to database
     */
    fun insertMusic(music: Music) {
        val albumId = insertAlbumSafely(music.album)
        val musicEntity = mapper.convertToMusicEntity(music, albumId)
        val musicId = musicDao.insertMusic(musicEntity)
        if (musicId == -1L) { // insert failed
            val musicOld = musicDao.getMusicByPlayUri(musicEntity.playUri)
            if (musicOld.copy(id = musicEntity.id) == musicEntity) {
                return
            } else {
                deleteMusic(music)
                insertMusic(music)
            }
            return
        }
        //insert artist
        val artistIds = insertArtistSafely(music.artist)

        //insert relation of artist and music
        artistIds.map { MusicArtistRelation(musicId, it) }.let { musicDao.insertMusicArtist(it) }
        Table.Music.postChange()
    }


    fun deleteMusic(music: Music,
                    isDeleteFromDisk: Boolean = false) {

        //delete all info for this music
        database.openHelper.writableDatabase.inTransaction {
            delete("relation_music_artist", "music_id = ?", arrayOf(music.id))
            delete("relation_music_playlist", "music_id = ?", arrayOf(music.id))
            delete("entity_music", "id = ?", arrayOf(music.id))
        }

        //remove unlinked artists
        music.artist
                .map(mapper::convertToArtistEntity)
                .filter {
                    musicDao.getMusicByArtist(it.id).isEmpty()
                }
                .let {
                    Table.Artist.postChange()
                    musicDao.removeArtist(it)
                }

        //remove unlinked album
        if (musicDao.getMusicByAlbum(music.album.id).isEmpty()) {
            musicDao.removeAlbum(mapper.convertToAlbumEntity(music.album))
            Table.Album.postChange()
        }

        //remove from disk
        if (isDeleteFromDisk) {
            val file = File(music.playUri.getOrNull(0)?.uri)
            if (file.exists()) {
                file.delete()
            }
        }
        Table.Music.postChange()
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
    fun getArtists(): Deferred<List<Artist>> = async {
        musicDao.getArtists().map(mapper::convertToArtist)
    }

    private fun insertAlbumSafely(album: Album): Long {
        val albumEntity = mapper.convertToAlbumEntity(album)
        val albumId = musicDao.insertAlbum(albumEntity)
        Table.Album.postChange()
        return if (albumId == -1L) {
            musicDao.getAlbumByName(album.name).id
        } else {
            albumId
        }
    }

    /**
     * insert artist to table [tech.summerly.quiet.service.local.database.entity.ArtistEntity]
     * return the id which artist insert to
     */
    private fun insertArtistSafely(artists: List<Artist>): List<Long> {
        val ids = artists.map(mapper::convertToArtistEntity)
                .map { artistEntity ->
                    val id = musicDao.insertArtist(artistEntity)
                    if (id == -1L) {
                        musicDao.getArtistByName(artistEntity.name).id
                    } else {
                        id
                    }
                }
        Table.Artist.postChange()
        return ids
    }

    fun getPlaylists() = async {
        val list = ArrayList<Playlist>()
        database.openHelper.readableDatabase.inTransaction {
            musicDao.getPlaylists().forEach {
                val cursor = query("select count(*) from relation_music_playlist where playlist_id = ?", arrayOf(it.id))
                cursor ?: return@forEach
                cursor.moveToFirst()
                val count = cursor.getInt(0)
                list.add(mapper.convertToPlaylist(it, count))
                cursor.close()
            }
        }
        return@async list
    }

    fun createPlaylist(title: String) = async {
        require(title.isNotEmpty())
        //first check if name is exist
        if (musicDao.getPlaylistByTitle(title) != null) {
            return@async -2
        }
        val playlist = PlaylistEntity(id = 0, title = title, coverUri = null)
        val id = musicDao.insertPlaylist(playlist)
        if (id == -1L) {
            return@async -1
        }
        Table.Playlist.postChange()
        return@async 0
    }

    fun insertMusic(playlist: Playlist, musics: Array<Music>) {
        val relations = musics.map {
            MusicPlaylistRelation(it.id, playlist.id)
        }
        musicDao.insertMusicPlaylist(relations)
        Table.PlaylistMusic.postChange()
    }

    suspend fun getMusicsByArtist(artist: Artist): List<Music> = async {
        return@async musicDao.getMusicByArtist(artist.id).map {
            val artists = musicDao.getArtistByMusic(it.id).map(mapper::convertToArtist)
            val album = mapper.convertToAlbum(musicDao.getAlbum(it.albumId))
            mapper.convertToMusic(it, artists, album)
        }
    }.await()

    /**
     * get all albums
     */
    fun getAlbums(): Deferred<List<Album>> {
        return async { musicDao.getTotalAlbums().map(mapper::convertToAlbum) }
    }

    fun getMusicsByAlbum(album: Album): Deferred<List<Music>> = async {
        musicDao.getMusicByAlbum(album.id).map {
            val artists = musicDao.getArtistByMusic(it.id).map(mapper::convertToArtist)
            mapper.convertToMusic(it, artists, album)
        }
    }
}