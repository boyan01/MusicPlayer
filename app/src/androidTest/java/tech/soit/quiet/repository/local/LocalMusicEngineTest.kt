package tech.soit.quiet.repository.local

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import tech.soit.quiet.repository.db.dao.LocalMusicDao
import tech.soit.quiet.utils.mock

class LocalMusicEngineTest {


    private lateinit var engine: LocalMusicEngine

    private val localMusicDao = mock<LocalMusicDao>()

    @Before
    fun setUp() {
        engine = LocalMusicEngine(localMusicDao)
    }


    @Test
    fun scan() {
        engine.scan()
        Mockito.verifyZeroInteractions(localMusicDao)
    }
}