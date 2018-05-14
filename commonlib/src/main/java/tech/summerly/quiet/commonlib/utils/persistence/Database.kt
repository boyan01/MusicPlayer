package tech.summerly.quiet.commonlib.utils.persistence

import android.arch.persistence.room.*
import tech.summerly.quiet.commonlib.LibModule
import java.io.Serializable

@Entity(tableName = "objects")
class ObjectWrapperEntity(
        @PrimaryKey
        val key: String,

        @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
        val data: ByteArray?
)

@Dao
abstract class ObjectsDao {

    @Query("select * from objects where `key` == :key")
    protected abstract fun findObjectWrapper(key: String): ObjectWrapperEntity?

    @Query("delete from objects")
    protected abstract fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insert(objectWrapperEntity: ObjectWrapperEntity)

    operator fun get(key: String): Any? {
        val wrapper = findObjectWrapper(key) ?: return null
        return SerializableUtils.parseBytes(wrapper.data)
    }

    operator fun set(key: String, any: Serializable?) {
        try {
            val wrapper = ObjectWrapperEntity(key, any?.bytes())
            insert(wrapper)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}

@Database(entities = [ObjectWrapperEntity::class], exportSchema = false, version = 1)
abstract class PropertiesDatabase : RoomDatabase() {

    abstract fun objectsDao(): ObjectsDao


    companion object {

        private const val NAME = "properties.db"

        val INSTANCE: PropertiesDatabase by lazy {
            Room.databaseBuilder(LibModule, PropertiesDatabase::class.java, NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
        }

    }

}