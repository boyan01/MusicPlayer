package tech.summerly.quiet.local.scanner

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.local_setting_scanner.view.*
import org.jetbrains.anko.support.v4.ctx
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.scanner.persistence.LocalMusicScannerSetting

/**
 * author : Summer
 * date   : 2017/10/25
 */
class LocalScannerSettingDialog : AppCompatDialogFragment() {

    private val setting by lazy {
        LocalMusicScannerSetting(ctx)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.local_setting_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //让Dialog占据屏幕 80% 的宽度
        val width = view.context.getScreenWidth()
        view.layoutParams = FrameLayout
                .LayoutParams(
                        (width * 0.8).toInt(),
                        FrameLayout.LayoutParams.MATCH_PARENT
                )
        view.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        view.checkboxFilterDuration.isChecked = setting.isFilterByDuration()
        view.checkboxFilterDuration.setOnCheckedChangeListener { _, isChecked ->
            setting.setFilterByDuration(isChecked)
        }
    }

    private fun Context.getScreenWidth(): Int {
        val point = Point()
        (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getSize(point)
        return point.x
    }

}