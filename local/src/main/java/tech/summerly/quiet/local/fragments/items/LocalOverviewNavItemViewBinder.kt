package tech.summerly.quiet.local.fragments.items

import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.local_item_nav.view.*
import tech.summerly.quiet.commonlib.utils.ItemViewBinder
import tech.summerly.quiet.local.R

/**
 * Created by summer on 17-12-27
 * item view for LocalOverviewFragment's navigation
 */
internal class LocalOverviewNavItemViewBinder(private val onItemClick: (Navigator) -> Unit)
    : ItemViewBinder<LocalOverviewNavItemViewBinder.Navigator>() {


    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): ViewHolder {
        return ViewHolder(R.layout.local_item_nav, parent, inflater)
    }


    override fun onBindViewHolder(holder: ViewHolder, item: Navigator) = with(holder.itemView) {
        textTitle.setText(item.title)
        image.setImageResource(item.icon)
        setOnClickListener {
            onItemClick(item)
        }
    }

    internal class Navigator(
            @StringRes val title: Int,
            @DrawableRes val icon: Int
    )

}