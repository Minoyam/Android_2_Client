package com.example.sport_planet.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object NetworkHelper {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        })
        .addInterceptor {
            val request = it.request()
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MDQ3NTQ2MjAsImlhdCI6MTYwNDY2ODIyMCwianRpIjoiNjAxZjhlZjUtMDlkZC00M2E4LWIxNDEtMmVkYzIzMzhmOWFiIiwidXNlcklkIjoiMSJ9.iGcz5rOzHLIOKaRFwic5gPSQkVGo894UE1cqJDOfd1KFs0h30Om7osRQ94wJaqIK0KZ8bP8zsTK2dE4QGcoHWw"
                )
                .build()
            it.proceed(request)
        }.build()

    /*
         현재 알렉스님과 소연님의 서버가 다르기때문에 baseUrl를 따로 설정해야돼서 임시 방편으로
         변수를 여러개 해서 사용합니다.
         추후에 합쳐지면 변수를 하나로 수정하는 작업을 진행 하겠습니다 - 민호 -
     */
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://ec2-54-180-29-231.ap-northeast-2.compute.amazonaws.com:8080") // 채팅
        //.baseUrl("http://ec2-54-180-29-231.ap-northeast-2.compute.amazonaws.com:8082")
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofit2 = Retrofit.Builder()
        .baseUrl("http://18.215.230.51:8080")
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: Api = retrofit.create(Api::class.java)
    val api2: Api = retrofit2.create(Api::class.java)
}