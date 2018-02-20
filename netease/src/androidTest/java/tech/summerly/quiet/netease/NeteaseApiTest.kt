package tech.summerly.quiet.netease

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.service.netease.NeteaseCloudMusicApi

/**
 * author: summerly
 * email: yangbinyhbn@gmail.com
 */
@RunWith(AndroidJUnit4::class)
class NeteaseApiTest {

    private val neteaseApi = NeteaseCloudMusicApi(InstrumentationRegistry.getTargetContext())

    @Test
    fun getMusicDetail() {
        runBlocking {
            val result = neteaseApi.getMusicDetail(530986445)
            log { result }
        }
    }

    @Test
    fun test() {
        log { "test" }
    }
}