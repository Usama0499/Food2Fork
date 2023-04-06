package com.mindbuffer.foodfork.di.module

import android.content.Context
import androidx.room.Room
import com.example.doctor.di.BaseUrlQualifier
import com.mindbuffer.foodfork.BuildConfig
import com.mindbuffer.foodfork.data.DataManager
import com.mindbuffer.foodfork.data.DataManagerImpl
import com.mindbuffer.foodfork.data.local.db.AppDatabase
import com.mindbuffer.foodfork.data.local.db.DbHelper
import com.mindbuffer.foodfork.data.local.db.DbHelperImpl
import com.mindbuffer.foodfork.data.remote.ApiHelper
import com.mindbuffer.foodfork.data.remote.ApiHelperImpl
import com.mindbuffer.foodfork.data.remote.ApiService
import com.mindbuffer.foodfork.di.DatabaseInfo
import com.mindbuffer.foodfork.utils.AppConstants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    /**
     ** Database
     **/
    @Provides
    @DatabaseInfo
    fun provideDatabaseName(): String {
        return DB_NAME
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@DatabaseInfo dbName: String, @ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDbHelper(dbHelperImpl: DbHelperImpl): DbHelper {
        return dbHelperImpl
    }



    /**
     ** Retrofit
     **/
    @Provides
    @BaseUrlQualifier
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", BuildConfig.AUTH_TOKEN)
                .build()
            chain.proceed(request)
        }

        return if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .build()

        } else {
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, @BaseUrlQualifier BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper {
        return apiHelper
    }



    /**
     ** DataManager
     **/
    @Provides
    @Singleton
    fun provideDataManager(dataManager: DataManagerImpl): DataManager {
        return dataManager
    }

}