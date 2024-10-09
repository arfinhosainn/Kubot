package com.example.kubot.di

import android.util.Log
import com.example.kubot.BuildConfig
import com.example.kubot.auth_feature.data.AuthApi
import com.example.kubot.auth_feature.data.AuthApi.Companion.createBearerTokenString
import com.example.kubot.auth_feature.data.repository.local.AuthDao
import com.example.kubot.core.data.remote.KubotApi
import com.example.kubot.core.util.Constants.BASE_URL
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.create
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideKotlinSerialization(): Converter.Factory {
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
        }

        return json.asConverterFactory(contentType)
    }

    ///////////////////////////////////////////
    // Networking (OkHttp & Retrofit)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @AuthDaoProdUsingBinds authDao: AuthDao,
    ): OkHttpClient {

        // Configure to Allow more simultaneous requests
        val dispatcher = Dispatcher(Executors.newFixedThreadPool(20))
        dispatcher.maxRequests = 20
        dispatcher.maxRequestsPerHost = 20

        val addHeadersInterceptor = Interceptor { chain ->
            runBlocking(Dispatchers.IO) {


                val requestBuilder = chain.request().newBuilder()

                // If AuthToken is valid, add it to the request.
                AuthApi.getAuthToken {
                    authDao.getAuthToken() // if invalid, attempt to fetch AuthToken from the AuthDao
                }?.let { authToken ->
                    requestBuilder
                        .addHeader("Authorization", createBearerTokenString(authToken))
                }

                val request = requestBuilder
                    .build()

                chain.proceed(request)
            }
        }

        val jsonPrettyPrinter = object : HttpLoggingInterceptor.Logger {
            private fun print(m: String) {
                Log.i("API", m)
            }

            override fun log(message: String) {

                if (message.startsWith("{") || message.startsWith("[")) try {
                    JSONObject(message)
                        .toString(2)
                        .take(500)
                        .also(::print)
                } catch (e: JSONException) {
                    print(message)
                }
                else print(message)

                if (message.length > 500) {
                    print("=== ...${message.takeLast(message.length - 300)}")
                    return print("=== ${message.length - 500} more characters ===")
                }
            }
        }

        val okHttpClientBuilder = OkHttpClient.Builder()
            .dispatcher(dispatcher)
            .addInterceptor(addHeadersInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .callTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)

        return if(BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor(jsonPrettyPrinter)
            logging.level = HttpLoggingInterceptor.Level.BODY
            //logging.level = HttpLoggingInterceptor.Level.HEADERS

            okHttpClientBuilder
                .addInterceptor(logging)
                .build()
        } else {
            okHttpClientBuilder
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideKubotApi(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): KubotApi {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(converterFactory)  // for serialization
            .build()
            .create()
    }


}