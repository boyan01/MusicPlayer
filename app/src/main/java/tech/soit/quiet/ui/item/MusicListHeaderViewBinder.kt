package tech.soit.quiet.ui.item

import androidx.core.view.isGone
import kotlinx.android.synthetic.main.header_music_list.view.*
import tech.soit.quiet.R
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.KViewHolder
import tech.soit.quiet.utils.TypeLayoutRes
import tech.soit.quiet.utils.component.support.setOnClickListenerAsync
import tech.soit.quiet.utils.component.support.string

class ItemMusicListHeader(
        val count: Int,
        val isShowSubscribeButton: Boolean,
        var isSubscribed: Boolean
)


@TypeLayoutRes(R.layout.header_music_list)
class MusicListHeaderViewBinder(
        private val onClicked: () -> Unit,
        private val onCollectionClicked: (suspend () -> Unit)? = null
) : KItemViewBinder<ItemMusicListHeader>() {

    override fun onBindViewHolder(holder: KViewHolder, item: ItemMusicListHeader) {
        with(holder.itemView) {
            textCollection.isGone = item.isShowSubscribeButton
            textCollection.setOnClickListenerAsync {
                onCollectionClicked?.invoke()
            }
            if (!item.isSubscribed) {
                textCollection.setText(R.string.add_to_collection)
            } else {
                textCollection.setText(R.string.collected)
            }
            textMusicCount.text = string(R.string.template_item_play_list_count, item.count)
            setOnClickListener {
                onClicked()
            }
        }

    }

}