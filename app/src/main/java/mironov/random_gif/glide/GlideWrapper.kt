package mironov.random_gif.glide

import android.content.Context
import android.widget.ImageView
import mironov.random_gif.R
import mironov.random_gif.model.GifObject


class GlideWrapper(val context: Context ) {


    fun checkGifAndPost(obj: GifObject,imageView: ImageView) {
        //Some request does not have .gif URL(API feature)
        //Preview picture is used instead
        var uri: String? = obj.getUri()
        if (uri == null) {
            uri = obj.getPreviewUri()!!
            addBitmap(uri,imageView)
        } else {
            addGif(uri,imageView)
        }
    }

    private fun addGif(uri: String?,imageView: ImageView) {
        GlideApp.with(context)
            .asGif()
            .placeholder(R.drawable.ic_time)
            .error(R.drawable.ic_error)
            .load(uri)
            .into(imageView)
    }


    private fun addBitmap(uri: String?,imageView: ImageView) {
        GlideApp.with(context)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.ic_time)
            .error(R.drawable.ic_error)
            .into(imageView)
    }

    fun clear()
    {
        /*
        GlideApp.with(context)
            .asBitmap()
            .load(R.drawable.ic_baseline_cleared_cache)
            .placeholder(R.drawable.ic_time)
            .error(R.drawable.ic_error)
            .into(imageView)
         */
        Thread {
            GlideApp.get(context).clearDiskCache()
        }.start()
    }
}
