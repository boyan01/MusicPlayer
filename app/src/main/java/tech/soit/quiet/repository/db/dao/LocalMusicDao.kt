package tech.soit.quiet.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import tech.soit.quiet.repository.db.entity.LocalMusic

@Dao
abstract class LocalMusicDao {

    @Query("select * from local_music")
    abstract fun getAllMusics(): LiveData<List<LocalMusic>>


    @Insert
    abstract fun insertMusic(music: LocalMusic): Long


    @Query("select * from local_music where artist = :artist")
    abstract fun getMusicsByArtist(artist: String): LiveData<List<LocalMusic>>


    @Query("select * from local_music where album = :album")
    abstract fun getMusicsByAlbum(album: String): LiveData<List<LocalMusic>>


}