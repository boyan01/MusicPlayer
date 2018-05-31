package tech.summerly.quiet.netease.adapters.main

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlinx.android.synthetic.main.netease_header_playlist.view.*
import tech.summerly.quiet.commonlib.utils.invisible
import tech.summerly.quiet.commonlib.utils.popupMenu
import tech.summerly.quiet.commonlib.utils.string
import tech.summerly.quiet.commonlib.utils.support.TypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.commonlib.utils.visible
import tech.summerly.quiet.netease.R

/**
 * 播放列表分组
 */
internal class PlaylistHeaderViewBinder(
        private val onHeaderClick: (header: PlaylistHeader, position: Int) -> Unit
) : TypedBinder<PlaylistHeader>() {
    override fun onBindViewHolder(holder: ViewHolder, item: PlaylistHeader) {
        holder.itemView.setOnClickListener {
            onHeaderClick(item, holder.adapterPosition)
        }
        holder.itemView.headerTitle.text = item.title
        holder.itemView.headerMore.setOnClickListener {
            createHeaderMenu(it)
        }
        if (item.state == PlaylistHeader.STATE_EXPANDED) {
            holder.itemView.iconExpandIndicator.rotation = 90f
        } else {
            holder.itemView.iconExpandIndicator.rotation = 0f
        }
        if (item.state == PlaylistHeader.STATE_LOADING) {
            holder.itemView.iconExpandIndicator.invisible()
            holder.itemView.progressBar.visible()
        } else {
            holder.itemView.iconExpandIndicator.visible()
            holder.itemView.progressBar.invisible()
        }
    }

    private fun createHeaderMenu(anchor: View) {
        popupMenu(anchor, R.menu.netease_popup_playlist_header) {
            true
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return PlaylistHeaderViewHolder(R.layout.netease_header_playlist, parent)
    }

}

enum class PlaylistHeader(var state: Int) {


    CREATED(1),
    SUBSCRIPTION(1);

    companion object {
        const val STATE_LOADING = 0
        const val STATE_EXPANDED = 1
        const val STATE_CLOSED = 2
    }

    val title: String
        get() = when (this) {
            CREATED -> string(R.string.netease_playlist_header_create)
            SUBSCRIPTION -> string(R.string.netease_playlist_header_collect)
        }

}


internal class PlaylistHeaderViewHolder(layoutId: Int, parent: ViewGroup) : ViewHolder(layoutId, parent) {


    companion object {

        private const val DURATION = 300L

        private var animatorExpandIndicator: Animator? = null

        fun finishAnimator() {
            animatorExpandIndicator?.end()
        }
    }

    fun animateIndicator(state: Int) {
        if (state == PlaylistHeader.STATE_EXPANDED) {
            itemView.iconExpandIndicator.animateToRotation(90f)
        } else {
            itemView.iconExpandIndicator.animateToRotation(0f)
        }
    }

    private fun ImageView.animateToRotation(r: Float) {
        if (rotation == r) {
            return
        }
        animatorExpandIndicator?.cancel()
        animatorExpandIndicator = ObjectAnimator.ofFloat(this, "rotation", rotation, r)
                .setDuration(DURATION)
        animatorExpandIndicator?.start()
    }
}