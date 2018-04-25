package tech.summerly.quiet.netease.utils

import okhttp3.HttpUrl
import tech.summerly.quiet.commonlib.utils.net.cookie.PersistentCookieStore
import tech.summerly.quiet.netease.NeteaseModule
import tech.summerly.quiet.netease.persistence.NeteasePreference

fun isLogin(): Boolean = NeteasePreference.getLoginUser() != null

fun logout() {
    NeteasePreference.saveLoginUser(null)
    val cookieStore = PersistentCookieStore(NeteaseModule)
    cookieStore.getCookies()
            .filter { it.domain() == "music.163.com" }
            .forEach { cookieStore.remove(HttpUrl.parse("http://music.163.com")!!, it) }
}