package tech.soit.quiet.ui.item

import kotlinx.android.synthetic.main.item_setting_header.view.*
import tech.soit.quiet.R
import tech.soit.typed.adapter.TypedBinder
import tech.soit.typed.adapter.ViewHolder
import tech.soit.typed.adapter.annotation.TypeLayoutResource

data class SettingHeader(
        val title: String
)

@TypeLayoutResource(R.layout.item_setting_header)
class SettingHeaderBinder : TypedBinder<SettingHeader>() {
    override fun onBindViewHolder(holder: ViewHolder, item: SettingHeader) {
        holder.itemView.textHeader.text = item.title
    }
}