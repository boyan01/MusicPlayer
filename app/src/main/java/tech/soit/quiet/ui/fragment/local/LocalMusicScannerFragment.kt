package tech.soit.quiet.ui.fragment.local

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_local_scanner.*
import tech.soit.quiet.AppContext
import tech.soit.quiet.R
import tech.soit.quiet.repository.db.QuietDatabase
import tech.soit.quiet.repository.local.LocalMusicEngine
import tech.soit.quiet.ui.fragment.base.BaseFragment
import tech.soit.quiet.utils.annotation.LayoutId
import tech.soit.quiet.utils.component.support.Status
import tech.soit.quiet.utils.component.support.string
import tech.soit.quiet.utils.doWithPermissions
import tech.soit.quiet.viewmodel.LocalScannerViewModel

/**
 * @author : summer
 * @date : 18-8-29
 */
@LayoutId(R.layout.fragment_local_scanner)
class LocalMusicScannerFragment : BaseFragment() {

    companion object {

        const val TAG = "LocalMusicScanner"

    }

    init {
        viewModelFactory = object : ViewModelProvider.AndroidViewModelFactory(AppContext) {

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(LocalScannerViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return LocalScannerViewModel(LocalMusicEngine(QuietDatabase.instance.localMusicDao())) as T
                }
                return super.create(modelClass)
            }

        }

    }

    private val viewModel by lazyViewModelInternal<LocalScannerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.fitsSystemWindows = true
        buttonStart.setOnClickListener { _ ->
            requireBaseActivity().doWithPermissions(READ_EXTERNAL_STORAGE, onDenied = { _ ->
                Toast.makeText(requireContext(), R.string.toast_read_storage_permission_denied, Toast.LENGTH_SHORT).show()
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
                requireBaseActivity().navigationTo(LocalMusicScannerSettingFragment.TAG) { LocalMusicScannerSettingFragment() }
                return@setOnMenuItemClickListener true
            }
            false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.newAdded.observe(this, Observer {
            it ?: return@Observer
            if (it.status == Status.SUCCESS) {
                textLoading.text = string(R.string.processing_local_scanner, it.requireData().title)
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


}