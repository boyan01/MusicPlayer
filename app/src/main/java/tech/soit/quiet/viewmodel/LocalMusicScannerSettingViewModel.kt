package tech.soit.quiet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import tech.soit.quiet.repository.setting.LocalScannerSettingRepository
import tech.soit.quiet.ui.item.SettingScannerFolderFilter
import tech.soit.quiet.utils.component.support.Resource
import tech.soit.quiet.utils.component.support.map
import tech.soit.quiet.utils.testing.OpenForTesting

@OpenForTesting
class LocalMusicScannerSettingViewModel : ViewModel() {

    fun setFilterByDuration(boolean: Boolean) {
        LocalScannerSettingRepository.setFilterByDuration(boolean)
    }

    fun isFilterByDuration() = LocalScannerSettingRepository.isFilterByDuration()

    fun getFileFilterData(): LiveData<Resource<List<SettingScannerFolderFilter>>> {
        return LocalScannerSettingRepository.getFolderFilterData()
                .map {
                    if (it == null) {
                        return@map Resource.loading<List<SettingScannerFolderFilter>>()
                    } else {
                        return@map Resource.success(it)
                    }
                }
    }

    fun updateFilterData(settingScannerFolderFilter: SettingScannerFolderFilter) {
        LocalScannerSettingRepository.editFilterData {
            put(settingScannerFolderFilter)
        }
    }

}