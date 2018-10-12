package tech.soit.quiet.ui.activity.cloud.viewmodel

import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.po.NeteasePlayListDetail
import tech.soit.quiet.repository.netease.NeteaseRepository
import java.lang.Exception

class CloudPlayListDetailViewModel(
        private val repository: NeteaseRepository = NeteaseRepository.instance
) : ViewModel() {


    suspend fun loadData(id: Long): NeteasePlayListDetail? {
        return try {
            repository.playListDetail(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}