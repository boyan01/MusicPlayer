package tech.summerly.quiet.commonlib.utils.download

import tech.summerly.quiet.commonlib.model.IMusic

class DownloadRequest(
        val music: IMusic,
        val bitrate: Int
) {

    fun buildId(): String = "${music::class.simpleName}_${music.id}"

}