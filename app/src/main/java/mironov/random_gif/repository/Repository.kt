package mironov.random_gif.repository;


import android.content.Context
import mironov.random_gif.model.GifObject
import mironov.random_gif.web.NetworkService
import retrofit2.Call
import java.util.*


class Repository {
    private var sharedRepository: SharedRepository
    private var cachedGifObjects: ArrayList<GifObject>

    constructor(context: Context) {
        this.sharedRepository = SharedRepository(context)
        cachedGifObjects = sharedRepository.getList()
    }

    fun getGifsListFromCache():ArrayList<GifObject>{
        return cachedGifObjects
    }

    fun addObject(obj: GifObject) {
        cachedGifObjects.add(obj)
    }

    fun getObject(position: Int): GifObject {
        return cachedGifObjects[position]
    }

    fun getObjectFromNetwork(): Call<GifObject?>? {
        return NetworkService
            .getJSONApi()
            .getGif()
    }

    fun saveListToMemory() {
        sharedRepository.saveList(cachedGifObjects)
    }

    fun getPosition(): Int {
        return cachedGifObjects.size - 1
    }

    fun clear() {
        sharedRepository.clearPrefs()
        cachedGifObjects.clear()
    }
}
