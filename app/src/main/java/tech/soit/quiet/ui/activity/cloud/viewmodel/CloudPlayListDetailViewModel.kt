package tech.soit.quiet.ui.activity.cloud.viewmodel

import tech.soit.quiet.model.vo.PlayListDetail
import tech.soit.quiet.utils.testing.OpenForTesting
import tech.soit.quiet.viewmodel.CloudViewModel

@OpenForTesting
class CloudPlayListDetailViewModel : CloudViewModel() {

    suspend fun loadData(id: Long): PlayListDetail? {
        return try {
            repository.playListDetail(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}