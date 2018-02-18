package tech.summerly.quiet.commonlib.service

import com.alibaba.android.arouter.facade.template.IProvider
import tech.summerly.quiet.commonlib.bean.Music

/**
 * Created by summer on 18-2-18
 */
interface NeteaseMusicService : IProvider {

    suspend fun searchMusic(keyword: String, offset: Int = 0, limit: Int = 30): List<Music>

}