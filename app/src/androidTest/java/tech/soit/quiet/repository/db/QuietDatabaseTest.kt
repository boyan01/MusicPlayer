package tech.soit.quiet.repository.db

import androidx.room.Room
import androidx.test.InstrumentationRegistry

class QuietDatabaseTest {

    companion object {

        val instance by lazy {
            Room
                    .inMemoryDatabaseBuilder(
                            InstrumentationRegistry.getContext(),
                            QuietDatabase::class.java)
                    .allowMainThreadQueries()
                    .build()
        }


    }

}