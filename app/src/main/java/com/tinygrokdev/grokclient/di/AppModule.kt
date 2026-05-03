package com.aicoder.grokclient.di

import android.content.Context
import com.aicoder.grokclient.data.api.XaiApiService
import com.aicoder.grokclient.data.local.SettingsRepository
import com.aicoder.grokclient.data.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideXaiApiService(okHttpClient: OkHttpClient): XaiApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.x.ai/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(XaiApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideChatRepository(apiService: XaiApiService): ChatRepository {
        return ChatRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext context: Context
    ): SettingsRepository {
        return SettingsRepository(context)
    }
}
