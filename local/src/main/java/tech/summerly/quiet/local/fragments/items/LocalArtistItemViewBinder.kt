package tech.summerly.quiet.local.fragments.items

import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.local_item_artist.view.*
import tech.summerly.quiet.commonlib.bean.Artist
import tech.summerly.quiet.commonlib.utils.GlideApp
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.commonlib.utils.getPictureUrl
import tech.summerly.quiet.commonlib.utils.getScreenWidth
import tech.summerly.quiet.local.R

/**
 * Created by summer on 17-12-24
 */
internal class LocalArtistItemViewBinder : ItemViewBinder<Artist>() {

    override fun onBindViewHolder(holder: ViewHolder, item: Artist) = with(holder.itemView) {
        GlideApp.with(this)
                .asBitmap()
                .load(item.getPictureUrl())
                .fitCenter()
                .override(getScreenWidth() / 3)
                .into(image)
        title.text = item.name
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.local_item_artist, parent, inflater)
    }

}