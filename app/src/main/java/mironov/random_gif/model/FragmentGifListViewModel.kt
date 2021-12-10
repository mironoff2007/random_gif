package mironov.random_gif.model

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import mironov.random_gif.repository.Repository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


public class FragmentGifListViewModel : ViewModel() {

    var position = -1
    var positionMax = -1

    var mutableStatus = MutableLiveData<Status>()
    private lateinit var repository: Repository
    private var gifObject: GifObject? = null


    public fun initRepository(context: Context) {
        repository = Repository(context)
        position = repository.getPosition()
        positionMax = position
    }

    fun getNext() {
        mutableStatus.postValue(Status.LOADING);
        if (positionMax == position) {
            repository.getObjectFromNetwork()
                ?.enqueue(object : Callback<GifObject?> {
                    override fun onResponse(
                        call: Call<GifObject?>,
                        response: Response<GifObject?>
                    ) {
                        if (response.body() != null) {
                            //put in repo
                            gifObject = response.body()
                            repository.addObject(gifObject!!)

                            position++;
                            positionMax = position;
                        }
                            mutableStatus.postValue(Status.DATA);
                    }

                    override fun onFailure(call: Call<GifObject?>, t: Throwable) {
                        mutableStatus.postValue(Status.ERROR)
                    }
                })
        } else {
            position++;
            gifObject = repository.getObject(position);
            mutableStatus.postValue(Status.DATA);
        }
    }

    fun getPrev() {
        position--;
        gifObject = repository.getObject(position);
        if (position == 0) {
            mutableStatus.postValue(Status.DATAFIRST);
        } else {
            mutableStatus.postValue(Status.DATA);
        }
    }

    fun getGifObject(): GifObject? {
        return gifObject;
    }

    fun getGifsListFromCache(): ArrayList<GifObject> {
        return repository.getGifsListFromCache()
    }

    fun clear() {
        repository.clear();
        position=-1
        positionMax=position
        mutableStatus.postValue(Status.CLEARCAHCE)
    }

    fun saveToPrefs() {
        repository.saveListToMemory()
    }

}


