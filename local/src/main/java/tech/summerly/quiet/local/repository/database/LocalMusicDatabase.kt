package tech.summerly.quiet.local.repository.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import tech.summerly.quiet.local.LocalModule
import tech.summerly.quiet.local.repository.dao.AlbumDao
import tech.summerly.quiet.local.repository.dao.ArtistDao
import tech.summerly.quiet.local.repository.dao.MusicDao
import tech.summerly.quiet.local.repository.entity.*

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
        version = 3,
        exportSchema = false
)
internal abstract class LocalMusicDatabase : RoomDatabase() {


    abstract fun musicDao(): MusicDao

    abstract fun artistDao(): ArtistDao

    abstract fun albumDao(): AlbumDao

    companion object {

        private const val DB_NAME = "local_music.db"

        val instance: LocalMusicDatabase by lazy {
            Room.databaseBuilder(LocalModule, LocalMusicDatabase::class.java, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }

    }
}