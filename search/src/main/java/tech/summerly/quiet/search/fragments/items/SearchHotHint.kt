package tech.summerly.quiet.search.fragments.items

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.search_item_chips.view.*
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.search.R
import tech.summerly.quiet.search.utils.ChipsLayout

/**
 * 热词
 */
class SearchHotHint(val hints: List<String>)

class SearchHotHintViewBinder(
        private val onChipClick: (hint: String) -> Unit
) : ItemViewBinder<SearchHotHint>() {

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.search_item_chips, parent, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: SearchHotHint) = with(holder.itemView) {
        chipLayout.removeAllViews()
        item.hints.forEach { text ->
            val button = generateChipItem(text, chipLayout)
            button.setOnClickListener {
                onChipClick(text)
            }
            chipLayout.addView(button)
        }
    }

}

fun generateChipItem(text: String, chipsLayout: ChipsLayout): TextView {
    val view = LayoutInflater.from(chipsLayout.context).inflate(R.layout.search_view_simple_chip, chipsLayout, false) as TextView
    view.text = text
    return view
}