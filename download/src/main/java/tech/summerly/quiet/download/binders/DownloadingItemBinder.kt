package tech.summerly.quiet.download.binders

import android.graphics.Rect
import android.support.v7.widget.AppCompatTextView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.download_item_downloading_back.view.*
import kotlinx.android.synthetic.main.download_item_downloading_fore.view.*
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.support.TypedBinder
import tech.summerly.quiet.commonlib.utils.support.ViewHolder
import tech.summerly.quiet.download.R

class DownloadItem

class DownloadingItemBinder() : TypedBinder<DownloadItem>() {

    private val mRect = Rect()
    private val mClipRect = Rect()

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder.from(R.layout.download_item_downloading, parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, item: DownloadItem) = with(holder.itemView) {

        layoutFore.findViewById<AppCompatTextView>(R.id.textTitle).text = "少年他的奇幻漂流"
        layoutBack.findViewById<AppCompatTextView>(R.id.textTitle).text = "少年他的奇幻漂流"

        layoutFore.findViewById<AppCompatTextView>(R.id.textProgress).text = "19m/56m"
        layoutBack.findViewById<AppCompatTextView>(R.id.textProgress).text = "19m/56m"

//        getGlobalVisibleRect(mRect)
//
//
        mRect.set(0, 0, width / 2, height)
        layoutFore.clipBounds = mRect

        log { "rect : $mRect" }
//
        mRect.set(width / 2, 0, width, height)
        layoutBack.clipBounds = mRect

        log { "rect : $mRect" }

    }

}