package tech.soit.quiet.utils.component.persistence

/**
 *
 * save key value pair to persistence
 *
 * @author : summer
 * @date : 18-8-20
 */
interface KeyValuePersistence {


    /**
     * get value by key
     */
    fun <T> get(key: String, cls: Class<T>): T?


    /**
     * save key and value
     */
    fun put(key: String, any: Any?)

}
