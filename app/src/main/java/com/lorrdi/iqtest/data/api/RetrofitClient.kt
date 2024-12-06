package com.lorrdi.iqtest.data.api

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitClient {
    private var interceptor: HttpLoggingInterceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.hh.ru/")
        .addConverterFactory(
            JacksonConverterFactory.create(
                ObjectMapper().registerKotlinModule().enable(
                    SerializationFeature.WRAP_ROOT_VALUE
                ).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            )
        )
        .client(client)
        .build()


    @Provides
    @Singleton
    fun provideHhApiService(retrofit: Retrofit): HhApiService =
        retrofit.create(HhApiService::class.java)
}
