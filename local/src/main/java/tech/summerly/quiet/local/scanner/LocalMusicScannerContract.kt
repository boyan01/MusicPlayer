package tech.summerly.quiet.local.scanner

import tech.summerly.quiet.commonlib.bean.Music
import tech.summerly.quiet.commonlib.mvp.BaseView

/**
 * author : Summer
 * date   : 2017/10/30
 */
class LocalMusicScannerContract {
    interface View : BaseView {
        val presenter: Presenter
        fun onAMusicScan(music: Music)
        fun onScannerError(msg: String?)
        fun onScannerComplete()
    }

    interface Presenter {

        val view: View

        fun startScannerJob()

        fun stopScanMusics()
    }
}