package tech.summerly.quiet.commonlib.utils

import java.security.MessageDigest

/**
 * author : yangbin10
 * date   : 2017/12/21
 */
fun String.md5(): String {

    val md = MessageDigest.getInstance("MD5")
    val array = md.digest(toByteArray())
    val sb = StringBuffer()
    for (i in array.indices) {
        sb.append(Integer.toHexString(array[i].toInt() and 0xFF or 0x100).substring(1, 3))
    }
    return sb.toString()

}