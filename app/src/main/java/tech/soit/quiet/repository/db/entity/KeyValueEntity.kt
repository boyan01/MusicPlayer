package tech.soit.quiet.repository.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

/**
 * @author : summer
 * @date : 18-8-20
 */
@Entity(tableName = "objects")
data class KeyValueEntity(
        @PrimaryKey
        val key: String,

        @ColumnInfo
        val data: String
) {

    private companion object {

        val GSON = Gson()

    }

    constructor(key: String, value: Any?) : this(key, GSON.toJson(value))


    fun <T> getValue(cls: Class<T>): T {
        return GSON.fromJson(data, cls)
    }

}