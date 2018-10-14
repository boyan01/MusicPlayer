package tech.soit.quiet.ui.activity.cloud.viewmodel

import tech.soit.quiet.model.po.NeteasePlayListDetail
import tech.soit.quiet.viewmodel.CloudViewModel

class CloudPlayListDetailViewModel : CloudViewModel() {

    suspend fun loadData(id: Long): NeteasePlayListDetail? {
        return try {
            repository.playListDetail(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}