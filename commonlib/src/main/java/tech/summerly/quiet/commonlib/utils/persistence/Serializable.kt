package tech.summerly.quiet.commonlib.utils.persistence

import android.util.Base64
import tech.summerly.quiet.commonlib.utils.log
import java.io.*

fun Serializable.toBase64String(): String {
    return try {
        return Base64.encodeToString(bytes(), Base64.DEFAULT)
    } catch (e: IOException) {
        ""
    }
}

fun Serializable.bytes(): ByteArray {
    val outputStream = ByteArrayOutputStream()
    val oos = ObjectOutputStream(outputStream)
    try {
        oos.writeObject(this)
        oos.flush()
        return outputStream.toByteArray()
    } catch (e: IOException) {
        log { e.printStackTrace() }
        throw e
    } finally {
        oos.close()
        outputStream.close()
    }
}


object SerializableUtils {


    fun parseBytes(byteArray: ByteArray?): Any? {
        if (byteArray == null) {
            return null
        }
        val inputStream = ByteArrayInputStream(byteArray)
        val ois = ObjectInputStream(inputStream)
        try {
            return ois.readObject()
        } catch (e: Exception) {
            log { e.printStackTrace() }
            return null
        } finally {
            ois.close()
            inputStream.close()
        }
    }

    fun <T : Serializable> parseString(base64: String): T? {
        var ois: ObjectInputStream? = null
        try {
            val bytes = Base64.decode(base64, Base64.DEFAULT)
            ois = ObjectInputStream(ByteArrayInputStream(bytes))
            return ois.readObject() as T
        } catch (e: Exception) {
            return null
        } finally {
            try {
                ois?.close()
            } catch (e: Exception) {

            }
        }
    }

}


