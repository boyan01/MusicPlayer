package tech.summerly.quiet.local.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import tech.summerly.quiet.local.database.entity.AlbumEntity
import tech.summerly.quiet.local.database.entity.ArtistEntity
import tech.summerly.quiet.local.database.entity.MusicEntity
import tech.summerly.quiet.local.database.entity.PlaylistEntity

/**
 * Created by summer on 17-12-21
 */
@Dao
internal interface MusicDao {

    @Query(value = "select entity_music.* from relation_music_artist " +
            "left join entity_music on entity_music.id = relation_music_artist.music_id " +
            "left join entity_artist on entity_artist.id = relation_music_artist.artist_id " +
            "where entity_artist.id = :artistId")
    fun getMusicByArtist(artistId: Long): List<MusicEntity>

    @Query(value = "select * from entity_music where album_id = :albumId")
    fun getMusicByAlbum(albumId: Long): List<MusicEntity>

    @Query(value = "select * from entity_music where id = :id limit 1")
    fun getMusicById(id: Long): MusicEntity

    @Query(value = "select entity_music.* from relation_music_playlist " +
            "left join entity_music on entity_music.id = relation_music_playlist.music_id " +
            "left join entity_playlist on entity_playlist.id = relation_music_playlist.playlist_id " +
            "where entity_playlist.id = :playlistId")
    fun getMusicByPlaylist(playlistId: Long): List<MusicEntity>

    @Insert
    fun insertMusic(musics: List<MusicEntity>): List<Long>

    @Insert
    fun insertAlbum(albums: List<AlbumEntity>): List<Long>

    @Insert
    fun insertArtist(artists: List<ArtistEntity>): List<Long>

    @Delete
    fun removeMusic(musics: List<MusicEntity>): Int

    @Delete
    fun removeArtist(artists: List<ArtistEntity>): Int

    @Delete
    fun removeAlbum(artists: List<ArtistEntity>): Int

    @Delete
    fun removePlaylist(playlist: PlaylistEntity): Int
}