package tech.soit.quiet.utils

import android.content.Context
import android.os.storage.StorageManager
import tech.soit.quiet.AppContext
import java.lang.reflect.Array
import java.lang.reflect.InvocationTargetException

/**
 * @param is_removable true返回外置存储卡路径 false返回内置存储卡的路径
 */
fun getStoragePath(is_removable: Boolean): String? {

    val mStorageManager = AppContext.getSystemService(Context.STORAGE_SERVICE) as StorageManager
    val storageVolumeClazz: Class<*>?
    try {
        storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
        val getVolumeList = mStorageManager.javaClass.getMethod("getVolumeList")
        val getPath = storageVolumeClazz!!.getMethod("getPath")
        val isRemovable = storageVolumeClazz.getMethod("isRemovable")
        val result = getVolumeList.invoke(mStorageManager)
        val length = Array.getLength(result)
        for (i in 0 until length) {
            val storageVolumeElement = Array.get(result, i)
            val path = getPath.invoke(storageVolumeElement) as String
            val removable = isRemovable.invoke(storageVolumeElement) as Boolean
            if (is_removable == removable) {
                return path
            }
        }
    } catch (e: ClassNotFoundException) {
        e.printStackTrace()
    } catch (e: InvocationTargetException) {
        e.printStackTrace()
    } catch (e: NoSuchMethodException) {
        e.printStackTrace()
    } catch (e: IllegalAccessException) {
        e.printStackTrace()
    }

    return null
}