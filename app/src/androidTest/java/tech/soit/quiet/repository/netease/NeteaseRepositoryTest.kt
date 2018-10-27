package tech.soit.quiet.repository.netease

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import tech.soit.quiet.utils.mock

@RunWith(AndroidJUnit4::class)
class NeteaseRepositoryTest {


    private val cloudMusicService: CloudMusicService = mock()

    private val neteaseRepository = NeteaseRepository(cloudMusicService)

    @Test
    fun testGetUserPlayerList() = runBlocking {
        Mockito.`when`(cloudMusicService.userPlayList(Mockito.anyMap())).thenReturn(CompletableDeferred(getRawJsonObject("user_playlist")))
        val playLists = neteaseRepository.getUserPlayerList(0)
        Assert.assertEquals(8 + 13, playLists.size)
    }

    /**
     * open /res/raw/$name.json file
     */
    private fun getRawJsonObject(name: String): JsonObject {
        val resources = InstrumentationRegistry.getInstrumentation().context.resources
        val inputStream = resources.openRawResource(resources.getIdentifier(name, "raw", "tech.soit.quiet.test"))
        return Gson().fromJson(inputStream.reader(), JsonObject::class.java)
    }
}