package tech.soit.quiet.utils.component.persistence

import tech.soit.quiet.repository.db.QuietDatabase

/**
 *
 * @see KeyValuePersistence
 *
 * @author : summer
 * @date : 18-8-20
 */
object KeyValue : KeyValuePersistence {

    private val impl: KeyValuePersistence get() = QuietDatabase.instance.keyValueDao()

    override fun <T> get(key: String, cls: Class<T>): T? {
        return impl.get(key, cls)
    }

    override fun put(key: String, any: Any?) {
        impl.put(key, any)
    }

    inline fun <reified T> get(key: String): T? {
        return get(key, T::class.java)
    }

}