package tech.summerly.quiet.local.fragments

import android.os.Bundle
import android.support.annotation.DrawableRes
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import tech.summerly.quiet.commonlib.base.BaseFragment

/**
 * provide an overview for LocalMusic : LocalFavMusic , LocalMusicMonitor
 */
class LocalOverviewFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return TextView(context).apply {
            text = "OverView"
            gravity = Gravity.CENTER
        }
    }

    /**
     * 固定在顶部的Tag
     */
    private data class PinedTag(val title: String, @DrawableRes val icon: Int)


}