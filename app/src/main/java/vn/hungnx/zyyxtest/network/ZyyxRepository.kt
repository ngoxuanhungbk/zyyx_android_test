package vn.hungnx.zyyxtest.network

import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import vn.hungnx.zyyxtest.model.SearchRepoResponse
import vn.hungnx.zyyxtest.model.SearchUserResponse
import java.util.concurrent.TimeUnit

class ZyyxRepository {
    companion object {
        const val BASE_URL = "https://api.github.com"
        private var instance: ZyyxRepository? = null

        fun getInstance(): ZyyxRepository {
            if (instance == null)
                instance = ZyyxRepository()
            return instance!!
        }
    }

    var apiService: ZyyxApiService

    init {
        apiService = provideRetrofit(provideOkhttpClient()).create(ZyyxApiService::class.java)
    }

    private fun provideOkhttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val loggingHeader = HttpLoggingInterceptor()
        loggingHeader.level = HttpLoggingInterceptor.Level.HEADERS

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor(loggingHeader)
        httpClient.connectTimeout(30, TimeUnit.SECONDS)
        httpClient.readTimeout(30, TimeUnit.SECONDS)
        val interceptor = Interceptor { chain ->
            var original = chain.request()
            var builder = original.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .method(original.method(), original.body())
            var originalHttpUrl = original.url()
            var url: HttpUrl = originalHttpUrl.newBuilder().build()

            builder.url(url)
            chain.proceed(builder.build())
        }
        httpClient.addInterceptor(interceptor)
        return httpClient.build()
    }

    private fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    fun searchGithubUserName(userName: String, observer: Observer<SearchUserResponse>) {
        apiService.searchUserName(userName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }

    fun searchGithubRepo(
        repo: String,
        isPolular: Boolean = false,
        observer: Observer<SearchRepoResponse>
    ) {
        apiService.searchRepo(repo,if(isPolular) "stars" else "updated")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }
}