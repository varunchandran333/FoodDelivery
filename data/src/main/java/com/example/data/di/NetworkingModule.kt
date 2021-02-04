package com.example.data.di

import com.example.data.apiServices.GetAllDataService
import com.example.fooddelivery.BuildConfig
import com.example.data.common.utils.ConnectivityImpl
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkingModule = module {

    var offlineInterceptor: Interceptor? = null
    val onlineInterceptor: Interceptor = object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val response: Response = chain.proceed(chain.request())
            val maxAge = 60 // read from cache for 60 seconds even if there is internet connection
            return response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }
    }
    single {
        offlineInterceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request: Request = chain.request()
                if (!get<ConnectivityImpl>().hasNetworkAccess()) {
                    val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
                    request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .removeHeader("Pragma")
                        .build()
                }
                return chain.proceed(request)
            }
        }
    }
    val cacheSize = 10 * 1024 * 1024 // 10 MB
    single { ConnectivityImpl(androidContext()) }
    single { Cache(androidContext().cacheDir, cacheSize.toLong()) }
    single { GsonConverterFactory.create() as Converter.Factory }
    single { HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY) }
    single {
        OkHttpClient.Builder().apply {
            cache(get())
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            offlineInterceptor?.let { interceptor -> addInterceptor(interceptor) }
            addNetworkInterceptor(onlineInterceptor)
        }.build()
    }
    single {
        Retrofit.Builder()
           .baseUrl(BuildConfig.HOST)
            .client(get())
            .addConverterFactory(get())
            .build()
    }
    single { get<Retrofit>().create(GetAllDataService::class.java) }
}