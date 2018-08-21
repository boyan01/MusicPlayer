package tech.soit.quiet.repository.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tech.soit.quiet.repository.db.entity.KeyValueEntity
import tech.soit.quiet.utils.component.persistence.KeyValuePersistence
import tech.summerly.quiet.commonlib.utils.LoggerLevel
import tech.summerly.quiet.commonlib.utils.log
import java.lang.reflect.Type

/**
 *
 * dao access object for [KeyValueEntity]
 *
 * it provide [get] and [put] to persist and restore data
 *
 * @author : summer
 * @date : 18-8-20
 */
@Dao
abstract class KeyValueDao : KeyValuePersistence {

    @Query("select * from objects where `key` == :key")
    protected abstract fun findEntity(key: String): KeyValueEntity?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insert(objectWrapperEntity: KeyValueEntity)

    override fun <T> get(key: String, typeofT: Type): T? {
        val entity = findEntity(key) ?: return null
        try {
            return entity.getValue(typeofT)
        } catch (e: Exception) {
            log(LoggerLevel.ERROR) { "parse key($key) failed : ${entity.data} " }
            put(key, null)
        }
        return null
    }

    override fun put(key: String, any: Any?) {
        try {
            val wrapper = KeyValueEntity(key, any)
            insert(wrapper)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}