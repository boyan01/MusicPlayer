package tech.summerly.quiet.netease.api.result


/**
 * author : SUMMERLY
 * time   : 2017/8/23
 * desc   :
 */
data class MusicUrlResultBean(

        val data: List<Datum>?,

        val code: Int
) {
    data class Datum(
            var id: Long,

            var url: String?,

            var bitrate: Int,

            var size: Long,

            var md5: String,

            var type: String?

    )
}

