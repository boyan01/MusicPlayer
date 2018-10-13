package tech.soit.quiet.ui.activity.local

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.activity_local_scanner.*
import tech.soit.quiet.R
import tech.soit.quiet.repository.local.LocalMusicEngine
import tech.soit.quiet.ui.activity.base.BaseActivity
import tech.soit.quiet.ui.fragment.local.LocalMusicScannerSettingFragment
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.log
import tech.soit.quiet.utils.component.support.QuietViewModelProvider
import tech.soit.quiet.utils.component.support.Status
import tech.soit.quiet.utils.component.support.string
import tech.soit.quiet.utils.doWithPermissions
import tech.soit.quiet.viewmodel.LocalScannerViewModel

@LayoutId(R.layout.activity_local_scanner)
class LocalScannerActivity : BaseActivity() {

    init {
        viewModelFactory = object : QuietViewModelProvider() {
            override fun createViewModel(modelClass: Class<ViewModel>): ViewModel {
                if (modelClass == LocalScannerViewModel::class.java) {
                    return LocalScannerViewModel(LocalMusicEngine(database.localMusicDao()))
                }
                return super.createViewModel(modelClass)
            }
        }
    }

    private val viewModel by lazyViewModel<LocalScannerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //init view
        buttonStart.setOnClickListener { _ ->
            doWithPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, onDenied = { _ ->
                Toast.makeText(this, R.string.toast_read_storage_permission_denied, Toast.LENGTH_SHORT).show()
            }) {
                viewModel.startScan()
            }
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        toolbar.inflateMenu(R.menu.menu_local_scanner)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_setting) {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.container, LocalMusicScannerSettingFragment())
                        .addToBackStack("setting")
                        .commit()
                return@setOnMenuItemClickListener true
            }
            false
        }

        subscribeViewModel()

    }

    private fun subscribeViewModel() {
        viewModel.newAdded.observe(this, Observer {
            it ?: return@Observer
            if (it.status == Status.SUCCESS) {
                textLoading.text = string(R.string.processing_local_scanner, it.requireData().getTitle())
            }
        })
        viewModel.status.observe(this, Observer { status: @LocalScannerViewModel.ScannerStatus Int ->
            when (status) {
                LocalScannerViewModel.STATUS_SCANNING -> {
                    resultLayout.isGone = true
                    scanningLayout.isVisible = true
                    buttonEnd.setText(R.string.stop_local_scanner)
                    buttonEnd.setOnClickListener {
                        viewModel.stopScan()
                    }

                }
                LocalScannerViewModel.STATUS_SUCCESS -> {
                    scanningLayout.isGone = true
                    resultLayout.isVisible = true
                    textResult.text = string(R.string.template_scanner_result, viewModel.resultCount)
                    buttonEnd.setText(R.string.end_local_scanner)
                    buttonEnd.setOnClickListener {
                        onBackPressed()
                    }
                }
                else -> {
                    buttonEnd.setText(R.string.end_local_scanner)
                    buttonEnd.setOnClickListener {
                        onBackPressed()
                    }
                    scanningLayout.isGone = true
                    resultLayout.isVisible = true
                    textResult.text = getString(R.string.local_scanner_hint)
                }
            }
        })
    }

    override fun onStop() {
        super.onStop()
        log { "LocalScannerActivity#onStop" }
    }

}
