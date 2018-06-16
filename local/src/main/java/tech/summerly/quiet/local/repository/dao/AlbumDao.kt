package tech.summerly.quiet.local.repository.dao

import android.arch.persistence.room.*
import tech.summerly.quiet.local.repository.entity.AlbumEntity
import tech.summerly.quiet.local.repository.entity.MusicArtistRelation

@Dao
abstract class AlbumDao {


    @Query("select * from entity_album")
    abstract fun allAlbum(): List<AlbumEntity>

    @Query("select * from entity_album where id = :id")
    abstract fun album(id: Long): AlbumEntity

    @Delete
    abstract fun removeAlbum(album: AlbumEntity): Int

    @Insert
    abstract fun insertAlbum(album: AlbumEntity): Long

}