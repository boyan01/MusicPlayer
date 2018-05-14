package tech.summerly.quiet.commonlib.utils.persistence

import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import java.io.Serializable

@RunWith(AndroidJUnit4::class)
class SerializableUtilsTest {


    @Test
    fun serialize() {
        val obj = TestObject()
        val bytes = obj.bytes()

        assertTrue(bytes.isNotEmpty())

        val obj2 = SerializableUtils.parseBytes(bytes)
        obj2 as TestObject
        assertTrue(obj != obj2)
        assertEquals(obj.message, obj2.message)

    }


    class TestObject : Serializable {

        val message: String = "haha"

    }

}