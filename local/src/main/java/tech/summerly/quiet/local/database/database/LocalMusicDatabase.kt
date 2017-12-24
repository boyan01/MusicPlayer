package tech.summerly.quiet.local.database.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import tech.summerly.quiet.local.database.dao.MusicDao
import tech.summerly.quiet.local.database.entity.*

/**
 * Created by summer on 17-12-21
 */
@Database(
        entities = [
            MusicEntity::class,
            ArtistEntity::class, MusicArtistRelation::class,
            AlbumEntity::class,
            PlaylistEntity::class, MusicPlaylistRelation::class
        ],
        version = 2,
        exportSchema = false
)
internal abstract class LocalMusicDatabase : RoomDatabase() {


    abstract fun musicDao(): MusicDao

    companion object {
        private const val DB_NAME = "local_music.db"

        private var instance: LocalMusicDatabase? = null

        fun getInstance(context: Context) = instance ?:
                Room.databaseBuilder(context, LocalMusicDatabase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build()
                        .also {
                            instance = it
                        }
    }
}