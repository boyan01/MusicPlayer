package tech.soit.quiet.repository.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import tech.soit.quiet.ui.item.SettingScannerFolderFilter
import tech.soit.quiet.utils.component.persistence.KeyValue
import tech.soit.quiet.utils.component.persistence.get

/**
 * repository for LocalScannerSetting
 */
object LocalScannerSettingRepository {

    private const val KEY_IS_FILTER_BY_DURATION = "scanner_filter_by_duration"

    private const val KEY_FILE_FILTER = "_local_setting_scanner_filter_folder"

    private val isFilterByDuration = MutableLiveData<Boolean>()

    private val folderFilterData = MutableLiveData<List<SettingScannerFolderFilter>>()

    init {
        //初始化读取数据
        GlobalScope.launch {
            isFilterByDuration.postValue(KeyValue.get(KEY_IS_FILTER_BY_DURATION) ?: true)
            folderFilterData.postValue(KeyValue.get(KEY_FILE_FILTER, object : TypeToken<List<SettingScannerFolderFilter>>() {}.type))
        }
    }

    fun isFilterByDuration(): LiveData<Boolean> {
        return isFilterByDuration
    }

    fun setFilterByDuration(b: Boolean) {
        isFilterByDuration.postValue(b)
        GlobalScope.launch {
            KeyValue.put(KEY_IS_FILTER_BY_DURATION, b)
        }
    }

    fun getFolderFilterData(): LiveData<List<SettingScannerFolderFilter>> {
        return folderFilterData
    }

    /**
     * edit filter folder
     * [FilterEditorScope] contains put and remove operation to edit filter folders
     */
    fun editFilterData(editor: FilterEditorScope.() -> Unit) {
        val scope = FilterEditorScope()
        scope.editor()
        folderFilterData.postValue(scope.list)
        GlobalScope.launch {
            KeyValue.put(KEY_FILE_FILTER, scope.list)
        }
    }


    class FilterEditorScope {

        val list = ArrayList(folderFilterData.value ?: emptyList())

        fun put(filter: SettingScannerFolderFilter) {
            val index = list.indexOfFirst { it.path == filter.path }
            if (index == -1) {
                list.add(filter)
                return
            }
            list[index] = filter
        }

        fun remove(filter: SettingScannerFolderFilter) {
            list.removeAll { it.path == filter.path }
        }

    }

}