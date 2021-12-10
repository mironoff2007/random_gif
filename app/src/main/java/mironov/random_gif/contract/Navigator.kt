package mironov.random_gif.contract

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import mironov.random_gif.model.GifObject

typealias ResultListener<T> = (T) -> Unit

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showGifInfoScreen(gif:GifObject){}

    fun <T : Parcelable> showGif(result: T)

}