package tech.summerly.quiet.local.repository.dao

import android.arch.persistence.room.*
import tech.summerly.quiet.local.repository.entity.*

/**
 * Created by summer on 17-12-21
 */
@Dao
internal interface MusicDao {

    @Query("select * from entity_music")
    fun getTotalMusics(): List<MusicEntity>


    @Query(value = "select entity_artist.* from relation_music_artist " +
            "left join entity_music on entity_music.id = relation_music_artist.music_id " +
            "left join entity_artist on entity_artist.id = relation_music_artist.artist_id " +
            "where entity_music.id = :musicId")
    fun getArtistByMusic(musicId: Long): List<ArtistEntity>

    @Query(value = "select entity_music.* from relation_music_artist " +
            "left join entity_music on entity_music.id = relation_music_artist.music_id " +
            "left join entity_artist on entity_artist.id = relation_music_artist.artist_id " +
            "where entity_artist.id = :artistId")
    fun getMusicByArtist(artistId: Long): List<MusicEntity>

    @Query(value = "select * from entity_album where id = :id")
    fun getAlbum(id: Long): AlbumEntity

    @Query(value = "select * from entity_music where album_id = :albumId")
    fun getMusicByAlbum(albumId: Long): List<MusicEntity>

    @Query(value = "select * from entity_music where id = :id limit 1")
    fun getMusicById(id: Long): MusicEntity

    @Query(value = "select * from entity_music where playUri = :uri limit 1")
    fun getMusicByPlayUri(uri: String): MusicEntity

    @Query(value = "select entity_music.* from relation_music_playlist " +
            "left join entity_music on entity_music.id = relation_music_playlist.music_id " +
            "left join entity_playlist on entity_playlist.id = relation_music_playlist.playlist_id " +
            "where entity_playlist.id = :playlistId")
    fun getMusicByPlaylist(playlistId: Long): List<MusicEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMusic(music: MusicEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAlbum(album: AlbumEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertArtist(artists: ArtistEntity): Long

    @Delete
    fun removeMusic(musics: List<MusicEntity>): Int

    @Delete
    fun removeArtist(artists: List<ArtistEntity>): Int

    @Delete
    fun removeAlbum(album: AlbumEntity): Int

    @Delete
    fun removePlaylist(playlist: PlaylistEntity): Int

    @Insert
    fun insertMusicArtist(relations: List<MusicArtistRelation>)

    @Query("select * from entity_artist")
    fun getArtists(): List<ArtistEntity>

    @Query("select * from entity_artist where name = :name")
    fun getArtistByName(name: String): ArtistEntity

    @Query("select * from entity_album where name = :name")
    fun getAlbumByName(name: String): AlbumEntity

    @Query("select * from entity_playlist")
    fun getPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMusicPlaylist(relations: List<MusicPlaylistRelation>)

    @Query("select * from entity_playlist where title = :title")
    fun getPlaylistByTitle(title: String): PlaylistEntity?

    @Insert
    fun insertPlaylist(playlist: PlaylistEntity): Long

    @Query("select * from entity_album")
    fun getTotalAlbums(): List<AlbumEntity>
}