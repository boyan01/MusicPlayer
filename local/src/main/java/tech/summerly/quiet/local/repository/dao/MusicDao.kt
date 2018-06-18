package tech.summerly.quiet.local.repository.dao

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Transformations
import android.arch.persistence.room.*
import tech.summerly.quiet.local.repository.entity.MusicEntity

/**
 * data access object for MusicEntity
 */
@Dao
internal abstract class MusicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertMusic(music: MusicEntity): Long


    @Query("select * from entity_music")
    protected abstract fun allMusicInternal(): LiveData<List<MusicEntity>>


    @Transaction
    open fun allMusic(): LiveData<List<MusicEntity>> {
        return Transformations.map(allMusicInternal()) { musics ->
            musics.forEach {
                it.album
                it.artist
            }
            musics
        }
    }

}