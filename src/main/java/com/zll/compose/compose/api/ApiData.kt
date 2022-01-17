package com.zll.compose.compose.api

import com.zll.compose.compose.util.DataStoreUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiData {
    private const val BASE_URL = "https://www.wanandroid.com/"

    private var retrofit: Retrofit

    private const val SAVE_USER_LOGIN_KEY = "user/login"
    private const val SAVE_USER_REGISTER_KEY = "user/register"
    private const val SET_COOKIE_KEY = "set-cookie"
    private const val COOKIE_NAME = "Cookie"

    private val mOkClient: OkHttpClient = OkHttpClient.Builder()
        //完整请求超时时长，从发起到接受返回数据，默认0s
        .callTimeout(20, TimeUnit.SECONDS)
        //与服务器建立链接的时长，默认10s
        .connectTimeout(20, TimeUnit.SECONDS)
        //读取服务器返回数据的时长
        .readTimeout(20, TimeUnit.SECONDS)
        //向服务器写入数据的时长，默认10s
        .writeTimeout(20, TimeUnit.SECONDS)
        //是否重连
        .retryOnConnectionFailure(true)
        //是否重定向
        .followRedirects(false)
        //保存cookie
        .addInterceptor {
            val request = it.request()
            val response = it.proceed(request)
            val requestUrl = request.url().toString()
            val domain = request.url().host()
            //cookie可能有多个，都保存下来
            if ((requestUrl.contains(SAVE_USER_LOGIN_KEY) ||
                        requestUrl.contains(SAVE_USER_REGISTER_KEY))
                && response.headers(SET_COOKIE_KEY).isNotEmpty()
            ) {
                val cookies = response.headers(SET_COOKIE_KEY)
                val cookie = encodeCookie(cookies)
                saveCookie(requestUrl, domain, cookie)
            }
            response
        }
        //请求时设置cookie
        .addInterceptor {
            val request = it.request()
            val builder = request.newBuilder()
            val domain = request.url().host()
            //获取domain内的cookie
            if (domain.isNotEmpty()) {
                val sqDomain: String = DataStoreUtils.readStringData(domain, "")
                val cookie: String = if (sqDomain.isNotEmpty()) sqDomain else ""
                if (cookie.isNotEmpty()) {
                    builder.addHeader(COOKIE_NAME, cookie)
                }
            }
            it.proceed(builder.build())
        }
        //添加网络拦截器，打印日志
        .addNetworkInterceptor(KtHttpLogInterceptor {
            logLevel(KtHttpLogInterceptor.LogLevel.BODY)
        })
        .build()


    init {
        retrofit = createRetrofit()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(mOkClient)
            .build()
    }


    fun <T> createApi(t: Class<T>): T {
        return retrofit.create(t)
    }

    /**
     * 保存cookie
     */
    @Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
    private fun saveCookie(url: String?, domain: String?, cookies: String) {
        url ?: return
        DataStoreUtils.putSyncData(url, cookies)
        domain ?: return
        DataStoreUtils.putSyncData(domain, cookies)
    }


    /**
     * 整理cookie
     */
    private fun encodeCookie(cookies: List<String>): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies
            .map { cookie ->
                cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }
            .forEach { it ->
                it.filterNot { set.contains(it) }.forEach { set.add(it) }
            }

        val ite = set.iterator()
        while (ite.hasNext()) {
            val cookie = ite.next()
            sb.append(cookie).append(";")
        }

        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }

        return sb.toString()
    }

}