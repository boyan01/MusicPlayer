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

        fun onMusicScanned(music: Music)
        fun onScannerError(msg: String?)
        fun onScannerComplete()
        fun onScannerStart()
    }

    interface Presenter {

        val view: View

        fun scanMusics()

        fun stopScanMusics()
    }
}