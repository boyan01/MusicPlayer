package tech.soit.quiet.repository.netease

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.soit.quiet.AppContext
import tech.soit.quiet.utils.component.network.CookieStore
import tech.soit.quiet.utils.component.network.PersistentCookieStore
import tech.soit.quiet.utils.component.network.randomUserAgent
import java.io.File

/**
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/22
 * desc   : 改自 https://github.com/Binaryify/NeteaseCloudMusicApi/blob/master/util/util.js
 */
internal object CloudMusicServiceProvider {

    private const val URL_BASE = "http://music.163.com"

    private val cache = File(AppContext.externalCacheDir, "netCache")

    /**
     * @param cookieStore 保存和取得 cookie 的载体
     */
    fun provideCloudMusicService(cookieStore: CookieStore? = PersistentCookieStore(AppContext)): CloudMusicService {
        return Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(generateClient(cookieStore))
                .build()
                .create(CloudMusicService::class.java)
    }


    private fun generateClient(cookieStore: CookieStore?): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(Cache(cache, 1024 * 1024 * 100))
                .addInterceptor {
                    val request = it.request()
                            .newBuilder()
                            .addHeader("Connection", "close")
                            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4")
                            .addHeader("Accept", "*/*")
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("Referer", "http://music.163.com")
                            .addHeader("Host", "music.163.com")
                            .addHeader("User-Agent", randomUserAgent())
                            .build()
                    it.proceed(request)
                }
                .cookieJar(object : CookieJar {
                    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
                        cookies.forEach {
                            cookieStore?.add(url, it)
                        }
                    }

                    override fun loadForRequest(url: HttpUrl): List<Cookie> {
                        return cookieStore?.get(url) ?: emptyList()
                    }
                })
                .build()
    }


}