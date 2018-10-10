package tech.soit.quiet.repository.netease

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.mock.Calls
import tech.soit.quiet.repository.db.await
import tech.soit.quiet.utils.component.support.Status
import tech.soit.quiet.utils.mock

@RunWith(AndroidJUnit4::class)
class NeteaseRepositoryTest {


    private val cloudMusicService: CloudMusicService = mock()

    private val neteaseRepository = NeteaseRepository(cloudMusicService)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testGetUserPlayerList() {
        Mockito.`when`(cloudMusicService.userPlayList(Mockito.anyMap())).thenReturn(Calls.response(getRawJsonObject("user_playlist")))
        val (status, data, message) = neteaseRepository.getUserPlayerList(0).await()

        Assert.assertEquals(Status.SUCCESS, status)
        data!!
        Assert.assertEquals(8 + 13, data.size)
        Assert.assertNull(message)
    }

    @Test
    fun testSuccessPreParse() {
        //read /raw/user_playlist.json file
        val jsonObject = getRawJsonObject("user_playlist")
        with(neteaseRepository) {
            val call = mock<Call<JsonObject>>()
            Mockito
                    .`when`(call.enqueue(Mockito.any()))
                    .then {
                        (it.getArgument(0) as Callback<JsonObject>).onResponse(call, Response.success(jsonObject))
                    }
            val playlistResource = call.preParse<JsonArray>("playlist").await()

            Assert.assertEquals(Status.SUCCESS, playlistResource.status)

            val playlist = playlistResource.data
            Assert.assertNotNull("playlist is not null", playlist)
            playlist!!
            Assert.assertEquals(8 + 13, playlist.size())
        }
    }

    @Test
    fun testErrorPreParse() {
        with(neteaseRepository) {
            val call = mock<Call<JsonObject>>()
            Mockito.`when`(call.enqueue(Mockito.any()))
                    .then {
                        (it.getArgument<Callback<JsonObject>>(0)).onFailure(call, Exception("error"))
                    }
            val playlistResource = call.preParse<JsonArray>("playlist").await()

            Assert.assertEquals(Status.ERROR, playlistResource.status)
            Assert.assertNull(playlistResource.data)
        }
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