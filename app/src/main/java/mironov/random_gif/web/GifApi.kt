package mironov.random_gif.web

import mironov.random_gif.model.GifObject
import retrofit2.http.GET
import retrofit2.Call;


interface GifApi {
    @GET("random?json=true")
    fun getGif(): Call<GifObject?>?
}
