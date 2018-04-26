package tech.summerly.quiet.commonlib.component.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isGone
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.view_music.view.*
import tech.summerly.quiet.commonlib.R
import tech.summerly.quiet.commonlib.bean.Music

class MusicView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {


    init {
        View.inflate(context, R.layout.view_music, this)
    }

    fun setMusic(music: Music) {

        textTitle.text = music.title
        textSubTitle.text = music.artistAlbumString()

        //indicator
        if (music.mvId == Music.ID_NONE) {
            indicatorMV.isGone = true
            indicatorMV.setOnClickListener(null)
        } else {
            indicatorMV.isVisible = true
            indicatorMV.setOnClickListener {
                //TODO
            }
        }

        val quality = music.getHighestQuality()
        if (quality.isNullOrEmpty()) {
            indicatorQuality.isGone = true
        } else {
            indicatorQuality.isVisible = true
            indicatorQuality.text = quality
        }

        textTitle.requestLayout()
    }

}