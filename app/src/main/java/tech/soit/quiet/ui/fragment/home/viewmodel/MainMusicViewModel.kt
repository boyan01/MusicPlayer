package tech.soit.quiet.ui.fragment.home.viewmodel

import androidx.lifecycle.ViewModel
import tech.soit.quiet.repository.netease.NeteaseRepository
import tech.soit.quiet.utils.testing.OpenForTesting

@OpenForTesting
class MainMusicViewModel : ViewModel() {

    fun getNeteaseRepository(): NeteaseRepository {
        return NeteaseRepository.instance
    }

}