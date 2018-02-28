package tech.summerly.quiet.service.netease.result

import com.google.gson.annotations.SerializedName

/**
 * Created by summer on 18-2-27
 */
data class RecordResultBean(
        @SerializedName(value = "data", alternate = ["weekData", "allData"])
        val data: List<Record>,
        val code: Int) {

    data class Record(val playCount: Int,
                      val score: Int,
                      val song: SongBean) {

        data class SongBean(val rtUrls: List<Any?>,
                            val ar: List<ArBean>,
                            val al: AlBean,
                            val st: Long,
                            val djId: Long,
                            val ftype: Long,
                            val rtype: Long,
                            var rurl: Any?,
                            var crbt: Any?,
                            var rtUrl: Any?,
                            val alia: List<Any?>,
                            val pop: Long,
                            var rt: Any?,
                            val t: Long,
                            val mv: Long,
                            val pst: Long,
                            val mst: Long,
                            val cp: Long,
                            val cf: String,
                            val dt: Long,
                            val h: HBean,
                            val l: LBean,
                            val cd: String,
                            val no: Long,
                            var a: Any?,
                            val m: MBean,
                            val fee: Long,
                            val v: Long,
                            val name: String,
                            val id: Long,
                            val privilege: PrivilegeBean) {


            data class ArBean(val id: Long,
                              val name: String)

            data class AlBean(val id: Long,
                              val name: String,
                              val pic_str: String,
                              val pic: Long)


            data class HBean(val br: Long,
                             val fid: Long,
                             val size: Long,
                             val vd: Float)

            data class LBean(val br: Long,
                             val fid: Long,
                             val size: Long,
                             val vd: Float)

            data class MBean(val br: Long,
                             val fid: Long,
                             val size: Long,
                             val vd: Float)

            data class PrivilegeBean(val id: Long,
                                     val fee: Long,
                                     val payed: Long,
                                     val st: Long,
                                     val pl: Long,
                                     val dl: Long,
                                     val sp: Long,
                                     val cp: Long,
                                     val subp: Long,
                                     val cs: Boolean,
                                     val maxbr: Long,
                                     val fl: Long,
                                     val toast: Boolean,
                                     val flag: Long,
                                     val preSell: Boolean)
        }
    }
}