package tech.soit.quiet.viewmodel

import androidx.lifecycle.ViewModel
import tech.soit.quiet.model.vo.User
import tech.soit.quiet.repository.netease.NeteaseRepository

abstract class CloudViewModel(
        protected val repository: NeteaseRepository = NeteaseRepository.instance
) : ViewModel() {

    /**
     * current user
     */
    fun getLoginUser(): User? {
        return repository.getLoginUser()
    }


}