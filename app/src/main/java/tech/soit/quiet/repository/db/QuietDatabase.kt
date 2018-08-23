package tech.soit.quiet.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.soit.quiet.AppContext
import tech.soit.quiet.repository.db.dao.KeyValueDao
import tech.soit.quiet.repository.db.entity.LocalMusic
import tech.soit.quiet.repository.db.dao.LocalMusicDao
import tech.soit.quiet.repository.db.entity.KeyValueEntity

@Database(
        entities = [LocalMusic::class, KeyValueEntity::class],
        version = 1,
        exportSchema = false
)
abstract class QuietDatabase : RoomDatabase() {

    abstract fun localMusicDao(): LocalMusicDao

    abstract fun keyValueDao(): KeyValueDao

    companion object {

        private const val DB_NAME = "quiet.db"

        val instance: QuietDatabase by lazy {
            Room.databaseBuilder(AppContext, QuietDatabase::class.java, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }

    }
}