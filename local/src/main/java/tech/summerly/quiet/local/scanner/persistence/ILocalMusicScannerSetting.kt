package tech.summerly.quiet.local.scanner.persistence

/**
 * author : Summer
 * date   : 2017/10/31
 */
interface ILocalMusicScannerSetting {

    fun isFilterByDuration(): Boolean

    fun getLimitDuration(): Int

    fun getAllFilterFolder(): Set<String>
}