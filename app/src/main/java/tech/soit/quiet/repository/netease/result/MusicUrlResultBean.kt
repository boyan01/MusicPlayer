package tech.summerly.quiet.data.netease.result

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/23
 * desc   :
 */
data class MusicUrlResultBean(

        @SerializedName("data")
        @Expose
        val data: List<Datum>?,

        @SerializedName("code")
        @Expose
        val code: Int
) {
    data class Datum(
            @SerializedName("id")
            @Expose
            var id: Long,

            @SerializedName("url")
            @Expose
            var url: String?,

            @SerializedName("br")
            @Expose
            var bitrate: Int,

            @SerializedName("size")
            @Expose
            var size: Long,

            @SerializedName("md5")
            @Expose
            var md5: String,

            @SerializedName("type")
            @Expose
            var type: String?

//        @SerializedName("code")
//        @Expose
//        var code: Long? = null,
//        @SerializedName("expi")
//        @Expose
//        var expi: Long? = null,
//        @SerializedName("gain")
//        @Expose
//        var gain: Double? = null,
//        @SerializedName("fee")
//        @Expose
//        var fee: Long? = null,
//        @SerializedName("uf")
//        @Expose
//        var uf: Any? = null,
//        @SerializedName("payed")
//        @Expose
//        var payed: Long? = null,
//        @SerializedName("flag")
//        @Expose
//        var flag: Long? = null,
//        @SerializedName("canExtend")
//        @Expose
//        var canExtend: Boolean? = null

    )
}

