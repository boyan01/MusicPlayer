package tech.soit.quiet.ui.item

import kotlinx.android.synthetic.main.item_setting_switch.view.*
import tech.soit.quiet.R
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder
import tech.soit.typed.adapter.annotation.TypeLayoutResource

data class SettingSwitch(
        val key: String,
        val title: String,
        val isChecked: Boolean,
        val subTitle: String? = null
)


@TypeLayoutResource(R.layout.item_setting_switch)
class SettingSwitchBinder : TypedBinder<SettingSwitch>() {

    override fun onBindViewHolder(holder: ViewHolder, item: SettingSwitch) {
        holder.itemView.switch1.apply {
            text = item.title
            isChecked = item.isChecked
        }
    }

}

