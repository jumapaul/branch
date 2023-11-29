package com.example.branch.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.branch.common.Constants.BASE_URL
import com.example.branch.common.Constants.DATABASE_NAME
import com.example.branch.common.TokenManager
import com.example.branch.data.local.MessageDatabase
import com.example.branch.data.remote.BranchApiService
import com.example.branch.data.remote.AuthInterceptor
import com.example.branch.data.repository.AuthRepositoryImpl
import com.example.branch.data.repository.MessageRepositoryImpl
import com.example.branch.domain.repository.AuthRepository
import com.example.branch.domain.repository.MessagesRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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

val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
    name = "data_store"
)

@Module
@InstallIn(SingletonComponent::class)
object MessagesModule {

    @Singleton
    @Provides
    fun provideTokenManager(@ApplicationContext context: Context): TokenManager =
        TokenManager(context)

    @Singleton
    @Provides
    fun provideAuthInterceptor(tokenManager: TokenManager): AuthInterceptor =
        AuthInterceptor(tokenManager)

    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())

    @Provides
    @Singleton
    fun provideMessagesApi(
        okHttpClient: OkHttpClient,
        retrofit: Retrofit.Builder
    ): BranchApiService =
        retrofit.client(okHttpClient).build().create(BranchApiService::class.java)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonBuilder(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideMessageRepository(api: BranchApiService, db: MessageDatabase): MessagesRepository {
        return MessageRepositoryImpl(api, db.messagesDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(api: BranchApiService, tokenManager: TokenManager): AuthRepository {
        return AuthRepositoryImpl(api, tokenManager)
    }

    @Singleton
    @Provides
    fun provideMessageDatabase(application: Application): MessageDatabase {
        return Room.databaseBuilder(
            application, MessageDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}