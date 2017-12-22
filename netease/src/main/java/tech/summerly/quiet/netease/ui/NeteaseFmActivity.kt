package tech.summerly.quiet.netease.ui

import android.os.Bundle
import kotlinx.android.synthetic.main.netease_activity_fm.*
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.netease.R

/**
 * author : yangbin10
 * date   : 2017/12/22
 */
class NeteaseFmActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.netease_activity_fm)
        listenEvent()
        initPlayer()
    }

    private fun listenEvent() {
        buttonDelete.setOnClickListener {

        }
        buttonLike.setOnClickListener {

        }

        buttonPlay.setOnClickListener {

        }
        buttonNext.setOnClickListener {

        }
        buttonComment.setOnClickListener {

        }
    }

    private fun initPlayer() {

    }
}