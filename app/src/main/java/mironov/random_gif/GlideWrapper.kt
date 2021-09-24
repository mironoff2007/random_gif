package mironov.random_gif

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide


class GlideWrapper {

    private lateinit var context: Context
    private lateinit var imageView: ImageView

    fun setContAndView(context: Context, imageView: ImageView)  {
        this.context = context
        this.imageView = imageView

    }


    fun addGif(uri: String?) {
        GlideApp.with(context)
            .asGif()
            .placeholder(R.drawable.ic_time)
            .error(R.drawable.ic_error)
            .load(uri)
            .into(imageView)
    }


    fun addBitmap(uri: String?) {
        GlideApp.with(context)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.ic_time)
            .error(R.drawable.ic_error)
            .into(imageView)
    }

    fun clear()
    {
        GlideApp.with(context)
            .asBitmap()
            .load(R.drawable.ic_baseline_cleared_cache)
            .placeholder(R.drawable.ic_time)
            .error(R.drawable.ic_error)
            .into(imageView)
        Thread {
            GlideApp.get(context).clearDiskCache()
        }.start()
    }
}
