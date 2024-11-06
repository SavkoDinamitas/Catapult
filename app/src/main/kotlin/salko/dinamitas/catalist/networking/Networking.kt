//package salko.dinamitas.catalist.networking
//
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.logging.HttpLoggingInterceptor
//import retrofit2.Retrofit
//import retrofit2.converter.kotlinx.serialization.asConverterFactory
//import salko.dinamitas.catalist.networking.serialization.AppJson
//
//val okHttpClient = OkHttpClient.Builder()
//    .addInterceptor {
//        val updatedRequest = it.request().newBuilder()
//            .addHeader("x-api-key", "live_5jaulTQeyJLbfqSYz8xrUdPCJkCv2IFhUNCC4NuXPHqK1t4okceJBI7rkYExVkE1")
//            .build()
//        it.proceed(updatedRequest)
//    }
//    .addInterceptor(
//        HttpLoggingInterceptor().apply {
//            setLevel(HttpLoggingInterceptor.Level.BODY)
//        }
//    )
//    .build()
//
//
//val retrofit: Retrofit = Retrofit.Builder()
//    .baseUrl("https://api.thecatapi.com/v1/")
//    .client(okHttpClient)
//    .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
//    .build()
//
//fun main(){
//    okHttpClient.newCall(Request.Builder().url("https://api.thecatapi.com/v1/breeds").build()).execute()
//}
//
