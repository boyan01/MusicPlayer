package tech.soit.quiet.utils.component.persistence

import tech.soit.quiet.repository.db.QuietDatabase
import java.lang.reflect.Type

/**
 *
 * @see KeyValuePersistence
 *
 * @author : summer
 * @date : 18-8-20
 */
object KeyValue : KeyValuePersistence {

    private val impl: KeyValuePersistence get() = QuietDatabase.instance.keyValueDao()

    override fun <T> get(key: String, typeofT: Type): T? {
        return impl.get(key, typeofT)
    }

    override fun put(key: String, any: Any?) {
        impl.put(key, any)
    }

}

/**
 * @see KeyValuePersistence.get
 */
inline fun <reified T> KeyValuePersistence.get(key: String): T? {
    return get(key, T::class.java)
}