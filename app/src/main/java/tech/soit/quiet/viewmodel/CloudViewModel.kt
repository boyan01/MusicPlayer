package tech.soit.quiet.viewmodel

import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.vo.User
import tech.soit.quiet.repository.netease.NeteaseRepository
import tech.soit.quiet.utils.testing.OpenForTesting

@OpenForTesting
abstract class CloudViewModel : ViewModel() {

    protected val repository: NeteaseRepository get() = NeteaseRepository.instance

    /**
     * current user
     */
    fun getLoginUser(): User? {
        return repository.getLoginUser()
    }


}