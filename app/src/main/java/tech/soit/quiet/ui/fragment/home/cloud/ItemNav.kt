package tech.soit.quiet.ui.fragment.home.cloud

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.android.synthetic.main.item_cloud_nav.view.*
import me.drakeet.multitype.MultiTypeAdapter
import tech.soit.quiet.R
import tech.soit.quiet.ui.activity.cloud.CloudDailyRecommendActivity
import tech.soit.quiet.ui.activity.cloud.TopDetailActivity
import tech.soit.quiet.ui.view.CircleOutlineProvider
import tech.soit.quiet.utils.KItemViewBinder
import tech.soit.quiet.utils.KViewHolder
import tech.soit.quiet.utils.TypeLayoutRes
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.withBinder
import tech.soit.typed.adapter.TypedAdapter


private class ItemNav(
        @StringRes val title: Int,
        @DrawableRes val icon: Int
)


@TypeLayoutRes(R.layout.item_cloud_nav)
private class ItemNavBinder : KItemViewBinder<ItemNav>() {

    private val circleOutlineProvider = CircleOutlineProvider()

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): KViewHolder {
        val holder = super.onCreateViewHolder(inflater, parent)
        holder.itemView.imageIcon.apply {
            outlineProvider = circleOutlineProvider
            clipToOutline = true
        }
        return holder
    }

    override fun onBindViewHolder(holder: KViewHolder, item: ItemNav) {
        with(holder.itemView) {
            imageIcon.setImageResource(item.icon)
            textTitle.setText(item.title)
            setOnClickListener {
                when (item.title) {
                    R.string.nav_radio -> {
                        log { "to radio" }
                    }
                    R.string.nav_daily_recommend -> {
                        context.startActivity(Intent(context, CloudDailyRecommendActivity::class.java))
                    }
                    R.string.nav_tends -> {
                        context.startActivity(Intent(context, TopDetailActivity::class.java))
                    }
                }
            }
        }
    }
}

/**
 * register navigator binders for [TypedAdapter]
 */
fun MultiTypeAdapter.withCloudNavigators(): MultiTypeAdapter {
    return withBinder(ItemNavBinder())
}


private val NAVIGATORS = listOf(
        ItemNav(R.string.nav_radio, R.drawable.ic_radio_black_24dp),
        ItemNav(R.string.nav_daily_recommend, R.drawable.ic_today_black_24dp),
        ItemNav(R.string.nav_tends, R.drawable.ic_show_chart_black_24dp)
)

/**
 * return the cloud navigator for [ItemNavBinder]
 * must use [withCloudNavigators] register binder first!!!
 *
 * the navigator include 【私人FM】 【每日推荐】 【排行榜】 since 2018/10/6
 *
 */
fun getCloudNavigators(): List<Any> {
    return NAVIGATORS
}