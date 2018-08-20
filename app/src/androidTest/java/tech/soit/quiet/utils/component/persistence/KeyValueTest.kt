package tech.soit.quiet.utils.component.persistence

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import tech.soit.quiet.repository.db.QuietDatabase
import tech.soit.quiet.repository.db.QuietDatabaseTest

/**
 * @author : summer
 * @date : 18-8-20
 */
class KeyValueTest {

    companion object {


        private val OBJECTS = listOf<Pair<String, Any?>>(
                "string" to "hallo",
                "int" to 1025,
                "long" to 2L,
                "double" to Double.MAX_VALUE,
                "float" to Float.MAX_VALUE,
                "boolean" to false,
                "char" to '2',
                "object" to StringPair("test8", "data")
        )


        data class StringPair(val first: String, val second: String)


    }

    private lateinit var database: QuietDatabase

    private lateinit var impl: KeyValuePersistence


    @get:Rule
    val r = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        database = QuietDatabaseTest.instance
        impl = database.keyValueDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSaveAndRestore() {
        OBJECTS.forEach { (key, value) ->
            impl.put(key, value)
        }
        assertEquals(1025, impl.get<Int>("int"))
        assertEquals("hallo", impl.get<String>("string"))
        assertEquals(Double.MAX_VALUE, impl.get<Double>("double"))
        assertEquals(Float.MAX_VALUE, impl.get<Float>("float"))
        assertEquals(false, impl.get<Boolean>("boolean"))
        assertEquals('2', impl.get<Char>("char"))
        assertEquals(StringPair("test8", "data"), impl.get<StringPair>("object"))

        OBJECTS.forEach { (key, _) ->
            impl.put(key, null)
        }
        assertEquals(null, impl.get<Int>("int"))
        assertEquals(null, impl.get<String>("string"))
        assertEquals(null, impl.get<Double>("double"))
        assertEquals(null, impl.get<Float>("float"))
        assertEquals(null, impl.get<Boolean>("boolean"))
        assertEquals(null, impl.get<Char>("char"))
        assertEquals(null, impl.get<StringPair>("object"))

    }


    inline fun <reified T> KeyValuePersistence.get(key: String): T? {
        return get(key, T::class.java)
    }
}