package mironov.random_gif

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import mironov.random_gif.R


class GlideWrapper(private val context: Context, private val imageView: ImageView) {
    fun addGif(uri: String?) {
        Glide.with(context)
            .asGif()
            .placeholder(R.drawable.ic_time)
            .error(R.drawable.ic_error)
            .load(uri)
            .into(imageView)
    }

    fun addBitmap(uri: String?) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .placeholder(R.drawable.ic_time)
            .error(R.drawable.ic_error)
            .into(imageView)
    }

    fun clear()
    {
        Glide.get(context).clearMemory()
    }
}
