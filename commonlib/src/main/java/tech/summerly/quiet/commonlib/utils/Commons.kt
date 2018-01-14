package tech.summerly.quiet.commonlib.utils

/**
 * format milli to MM:ss , ex : 12:00
 */
fun Number.toMusicTimeStamp(): String = with(toLong() / 1000) {
    val second = this % 60
    val minute = this / 60
    String.format("%02d:%02d", minute, second)
}