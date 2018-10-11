package tech.soit.quiet.ui.fragment.home.cloud

import kotlinx.android.synthetic.main.item_play_list.view.*
import tech.soit.quiet.R
import tech.soit.quiet.model.vo.PlayList
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.KViewHolder
import tech.soit.quiet.utils.TypeLayoutRes
import tech.soit.quiet.utils.component.ImageLoader
import tech.soit.quiet.utils.component.support.string

@TypeLayoutRes(R.layout.item_play_list)
class PlayListViewBinder : KItemViewBinder<PlayList>() {

    override fun onBindViewHolder(holder: KViewHolder, item: PlayList) {
        with(holder.itemView) {
            ImageLoader.with(this).load(item.getCoverImageUrl()).into(imageCover)
            textTitle.text = item.getName()
            textSubTitle.text = string(R.string.template_item_play_list_count, item.getTrackCount())
        }
    }

}