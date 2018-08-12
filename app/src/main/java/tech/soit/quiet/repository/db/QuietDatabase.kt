package tech.soit.quiet.repository.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import tech.soit.quiet.AppContext
import tech.soit.quiet.repository.db.po.LocalMusic
import tech.soit.quiet.repository.db.dao.LocalMusicDao

@Database(
        entities = [LocalMusic::class],
        version = 1,
        exportSchema = false
)
abstract class QuietDatabase : RoomDatabase() {

    abstract fun localMusicDao(): LocalMusicDao

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