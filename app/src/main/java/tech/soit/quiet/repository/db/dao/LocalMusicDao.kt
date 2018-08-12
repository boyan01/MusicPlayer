package tech.soit.quiet.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import tech.soit.quiet.repository.db.po.LocalMusic

@Dao
abstract class LocalMusicDao {

    @Query("select * from local_music")
    abstract fun getAllMusics(): List<LocalMusic>


    @Insert
    abstract fun insertMusic(music: LocalMusic): Long

}