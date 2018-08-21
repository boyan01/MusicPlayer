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
     * get value by key, might be null if value is empty or parse failed
     */
    fun <T> get(key: String, cls: Class<T>): T?


    /**
     * save key and value
     *
     * @param any null to remove the value
     *
     */
    fun put(key: String, any: Any?)

}
