package tech.summerly.quiet.local.ui

import android.arch.lifecycle.ViewModelProviders
import android.graphics.drawable.Animatable
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Build
import android.os.Bundle
import android.support.graphics.drawable.AnimatedVectorDrawableCompat
import android.view.View
import kotlinx.android.synthetic.main.local_activity_music_scanner.*
import kotlinx.android.synthetic.main.local_activity_music_scanner.view.*
import tech.summerly.quiet.commonlib.base.BaseActivity
import tech.summerly.quiet.commonlib.utils.gone
import tech.summerly.quiet.commonlib.utils.log
import tech.summerly.quiet.commonlib.utils.observe
import tech.summerly.quiet.local.R
import tech.summerly.quiet.local.viewmodel.MusicScannerViewModel

/**
 * Created by summer on 17-12-21
 */
internal class LocalMusicScannerActivity : BaseActivity() {


    private lateinit var scannerInfoView: ScannerInfoView

    private val viewModel by lazy {
        ViewModelProviders.of(this, MusicScannerViewModel).get(MusicScannerViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        setContentView(R.layout.local_activity_music_scanner)
        scannerInfoView = ScannerInfoView(scannerContainer)
        buttonStartScanner.setOnClickListener {
            viewModel.startScan()
        }
        buttonClose.setOnClickListener {
            onBackPressed()
        }
        textSearchSetting.setOnClickListener {
            LocalScannerSettingDialog().show(supportFragmentManager, "LocalScannerSetting")
        }
        viewModel.isScanning.observe(this) { isScanning ->
            if (isScanning == true) {
                scannerInfoView.setScanning()
            } else {
                scannerInfoView.setNotScanning()
            }
        }
        viewModel.newMusic.observe(this) { musicEntity ->
            if (viewModel.isScanning.value == true) {
                log { "on music scanner: $musicEntity" }
            }
        }
    }

    private class ScannerInfoView(private val view: View) {

        //设置视图显示状态处于扫描中
        fun setScanning() {
            view.buttonStartScanner.gone()
            view.textSearchSetting.visibility = View.GONE
            view.progressBar.visibility = View.VISIBLE
            (view.imageSearchProfile.drawable as Animatable).start()
        }

        fun setNotScanning() {
            view.buttonStartScanner.visibility = View.VISIBLE
            view.textSearchSetting.visibility = View.VISIBLE
            view.progressBar.visibility = View.INVISIBLE
            with(view.imageSearchProfile.drawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    (this as? AnimatedVectorDrawable)?.reset()
                            ?: (this as? AnimatedVectorDrawableCompat)?.stop()
                } else {
                    TODO("VERSION.SDK_INT < M")
                }

            }
        }
    }

}