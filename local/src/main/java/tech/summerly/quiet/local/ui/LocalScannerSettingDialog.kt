package tech.summerly.quiet.local.ui

import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.local_fragment_setting_scanner.view.*
import kotlinx.android.synthetic.main.local_item_scanner_setting_checkbox.view.*
import me.drakeet.multitype.MultiTypeAdapter
import org.jetbrains.anko.support.v4.ctx
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.commonlib.utils.getScreenWidth
import tech.summerly.quiet.commonlib.utils.gone
import tech.summerly.quiet.commonlib.utils.visible
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.ui.LocalScannerSettingDialog.CheckBoxPreference.Companion.valueOf
import tech.summerly.quiet.local.scanner.persistence.ScannerSetting

/**
 * author : Summer
 * date   : 2017/10/25
 *             //TODO 删除被标记为不扫描的音乐文件夹
 */
class LocalScannerSettingDialog : AppCompatDialogFragment() {

    private val setting by lazy {
        ScannerSetting(ctx)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.local_fragment_setting_scanner, container, false)
    }

    private val items = mutableListOf<Any>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //让Dialog占据屏幕 80% 的宽度
        val layoutParams = FrameLayout
                .LayoutParams(
                        (getScreenWidth() * 0.8).toInt(),
                        FrameLayout.LayoutParams.WRAP_CONTENT
                )
        view.layoutParams = layoutParams
        view.toolbar.setNavigationOnClickListener {
            dismiss()
        }
        initItems()
        view.list.adapter = MultiTypeAdapter(items).also {
            it.register(String::class.java, HeaderViewBinder())
            it.register(CheckBoxPreference::class.java, CheckBoxPreferenceViewBinder())
        }
    }

    private fun initItems() {
        items += getString(R.string.local_scanner_setting_title_filter_size)
        items += CheckBoxPreference(getString(R.string.local_scanner_setting_filter_duration),
                setting[ScannerSetting.KEY_FILTER_DURATION], ScannerSetting.KEY_FILTER_DURATION)
        items += CheckBoxPreference(getString(R.string.local_scanner_setting_filter_file_size),
                setting[ScannerSetting.KEY_FILTER_SIZE], ScannerSetting.KEY_FILTER_SIZE)
        items += getString(R.string.local_scanner_setting_title_filter_path)
        items.addAll(setting.getAllFolder().map(::valueOf))
    }

    private class HeaderViewBinder : ItemViewBinder<String>() {
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
            return ViewHolder(R.layout.local_item_scanner_setting_header, parent, inflater)
        }

        override fun onBindViewHolder(holder: ViewHolder, item: String) {
            (holder.itemView as TextView).text = item
        }
    }

    private inner class CheckBoxPreferenceViewBinder : ItemViewBinder<CheckBoxPreference>() {
        override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
            return ViewHolder(R.layout.local_item_scanner_setting_checkbox, parent, inflater)
        }

        override fun onBindViewHolder(holder: ViewHolder, item: CheckBoxPreference) = with(holder.itemView) {
            checkbox.isChecked = item.isChecked
            title.text = item.title
            if (item.summary == null) {
                summary.gone()
            } else {
                summary.visible()
                summary.text = item.summary
            }
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                updatePreference(item)
            }
            setOnClickListener {
                checkbox.isChecked = !checkbox.isChecked
            }
        }

    }

    private fun updatePreference(preference: CheckBoxPreference) {
        setting.put(preference.key, preference.isChecked)
    }

    private class CheckBoxPreference(
            val title: String,
            var isChecked: Boolean,
            val key: String = "",
            val summary: String? = null
    ) {
        companion object {
            fun valueOf(entry: Map.Entry<String, Boolean>): CheckBoxPreference {
                return CheckBoxPreference(
                        entry.key.substringAfterLast('/'),
                        entry.value,
                        entry.key,
                        entry.key.substringBeforeLast('/'))
            }
        }
    }
}