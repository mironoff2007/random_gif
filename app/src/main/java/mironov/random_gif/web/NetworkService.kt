package mironov.random_gif.web

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkService {

    //private var mInstance: NetworkService? = null
    private val BASE_URL = "https://developerslife.ru/"
    private lateinit var mRetrofit: Retrofit

   init {
       mRetrofit = Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()
   }


    fun getJSONApi(): GifApi {
        return mRetrofit.create<GifApi>(GifApi::class.java)
    }
}