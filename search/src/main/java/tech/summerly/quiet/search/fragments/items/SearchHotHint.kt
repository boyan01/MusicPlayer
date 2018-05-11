package tech.summerly.quiet.search.fragments.items

import android.support.design.chip.Chip
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.search_item_chips.view.*
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.search.R

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
        item.hints
                .map { text ->
                    val chip = Chip(context)
                    chip.chipText = text
                    chip.setOnClickListener {
                        onChipClick(text)
                    }
                    chipLayout.addView(chip)
                }
        Unit
    }

}