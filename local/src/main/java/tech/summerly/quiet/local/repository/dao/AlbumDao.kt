package tech.summerly.quiet.local.repository.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import tech.summerly.quiet.local.repository.entity.AlbumEntity
import tech.summerly.quiet.local.repository.entity.MusicEntity

@Dao
abstract class AlbumDao {


    @Query("select * from entity_album")
    abstract fun allAlbum(): LiveData<List<AlbumEntity>>

    @Query("select * from entity_album where id = :id")
    abstract fun album(id: Long): AlbumEntity

    @Delete
    abstract fun removeAlbum(album: AlbumEntity): Int

    @Insert
    abstract fun insertAlbum(album: AlbumEntity): Long

    @Query("select * from entity_music where album_id = :id")
    abstract fun musics(id: Long): List<MusicEntity>
}