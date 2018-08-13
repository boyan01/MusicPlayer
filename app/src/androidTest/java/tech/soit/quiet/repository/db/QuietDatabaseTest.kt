package tech.soit.quiet.repository.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import tech.soit.quiet.repository.db.entity.LocalMusic
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class QuietDatabaseTest {

    companion object {


        val DUMMY_MUSICS = listOf(
                LocalMusic(0, "file://test/01", "music01", "album01", "artist01"),
                LocalMusic(0, "file://test/02", "music02", "album02", "artist0102"),
                LocalMusic(0, "file://test/03", "music03", "album01", "artist01"),
                LocalMusic(0, "file://test/04", "music04", "album02", "artist0302"),
                LocalMusic(0, "file://test/05", "music05", "album01", "artist01")
        )

        val instance
            get() = Room
                    .inMemoryDatabaseBuilder(
                            InstrumentationRegistry.getContext(),
                            QuietDatabase::class.java)
                    .allowMainThreadQueries()
                    .build()

    }

}

/**
 * LiveData helper
 */
internal fun <T> LiveData<T>.await(): T {
    val o = Array<Any?>(1) { null }
    val countDownLatch = CountDownLatch(1)
    observeForever(object : Observer<T> {
        override fun onChanged(t: T) {
            o[0] = t
            countDownLatch.countDown()
            removeObserver(this)
        }
    })
    countDownLatch.await(2, TimeUnit.SECONDS)
    @Suppress("UNCHECKED_CAST")
    return o[0] as T
}