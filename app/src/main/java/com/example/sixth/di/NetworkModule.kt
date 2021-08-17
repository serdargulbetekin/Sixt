package com.example.sixth.di

import android.content.Context
import com.example.sixth.BuildConfig
import com.example.sixth.config.RequestCreator
import com.example.sixth.config.RequestExecutor
import com.example.sixth.config.SixtRequestExecutor
import com.example.sixth.constant.Constant.Companion.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideBaseUrl() = BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return logging
    }

    @Provides
    @Singleton
    fun provideRequestExecutor(
        context: Context,
        okhttpClient: OkHttpClient
    ) = RequestExecutor(
        context, okhttpClient, RequestCreator()
    )


    @Provides
    @Singleton
    fun providesSixtRequestExecutor(
        @ApplicationContext context: Context,
        okhttpClient: OkHttpClient
    ) =
        SixtRequestExecutor(context, okhttpClient, RequestCreator())
}
