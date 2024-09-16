package com.example.mvvmroom.module


import android.content.Context
import androidx.room.Room
import com.example.mvvmroom.BaseAppClass
import com.example.mvvmroom.interfaces.ApiInterface
import com.example.mvvmroom.repository.ApiRepository
import com.example.mvvmroom.room.dao.DeviceDao
import com.example.mvvmroom.room.database.DeviceDatabase
import com.example.mvvmroom.utils.Apis
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
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()

        httpClient.addInterceptor(Interceptor {

            val requestBuilder = it.request().newBuilder()

            val request = requestBuilder.build()
            return@Interceptor it.proceed(request)
        }).connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS).build()

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(logging)

        return httpClient.build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).baseUrl(Apis.BASE_URL)
            .client(okHttpClient).build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)


    @Provides
    @Singleton
    fun provideApiRepository(apiInterface: ApiInterface) = ApiRepository(apiInterface)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): DeviceDatabase {
        return Room.databaseBuilder(
            context,
            DeviceDatabase::class.java,
            "device_database"
        ).build()
    }

    @Provides
    fun provideDeviceDao(database: DeviceDatabase): DeviceDao {
        return database.deviceDao()
    }
}