package tech.summerly.quiet.local.scanner.persistence

/**
 * author : Summer
 * date   : 2017/10/31
 */
internal interface IScannerSetting {

    fun isFilterByDuration(): Boolean

    fun getLimitDuration(): Int

    fun getAllFilterFolder(): Set<String>

    fun put(key: String, value: Boolean)
}