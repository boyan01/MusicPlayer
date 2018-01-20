package tech.summerly.quiet.local.scanner

import kotlinx.coroutines.experimental.Job
import tech.summerly.quiet.commonlib.mvp.BaseView

/**
 * author : Summer
 * date   : 2017/10/30
 */
class LocalMusicScannerContract {
    interface View : BaseView {
        val presenter: Presenter
        fun onScannerError(msg: String?)
        fun onScannerComplete()
        fun onMusicScan(folder: String, name: String)
    }

    interface Presenter {

        val view: View

        fun scan(path: String): Job
    }
}