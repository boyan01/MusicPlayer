package tech.summerly.quiet.service.netease

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.summerly.quiet.commonlib.utils.net.cookie.CookieStore
import tech.summerly.quiet.commonlib.utils.randomUserAgent

/**
 * author : SUMMERLY
 * e-mail : yangbinyhbn@gmail.com
 * time   : 2017/8/22
 */
internal class CloudMusicServiceProvider {
    companion object {
        private const val URL_BASE = "http://music.163.com"
    }

    /**
     * @param cookieStore 保存和取得 cookie 的载体
     * @param cache 缓存
     */
    fun provideCloudMusicService(cookieStore: CookieStore? = null,
                                 cache: Cache): CloudMusicService {
        return Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(generateClient(cookieStore, cache))
                .build()
                .create(CloudMusicService::class.java)
    }


    private fun generateClient(cookieStore: CookieStore?, cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
                .cache(cache)
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
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .retryOnConnectionFailure(true)
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