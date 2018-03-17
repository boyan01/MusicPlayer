package tech.summerly.quiet.search

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.snappydb.DBFactory
import org.junit.Test
import org.junit.runner.RunWith
import tech.summerly.quiet.commonlib.utils.log
import kotlin.system.measureTimeMillis

/**
 * Created by summer on 18-3-6
 */

@RunWith(AndroidJUnit4::class)
class DbPerformanceTest {

    @Test
    fun test() {
        val context = InstrumentationRegistry.getTargetContext()
        val db = DBFactory.open(context, "test")
        val millis = measureTimeMillis {
            repeat(10_000) {
                db.put("f$it", "teajfiosaioghioahgioajsgj")
            }
        }
        log { "millis : $millis" }
        assert(millis == 0L)
    }


}