package mironov.random_gif.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import mironov.random_gif.databinding.ItemGifBinding
import mironov.random_gif.glide.GlideWrapper
import mironov.random_gif.model.GifObject
import java.util.ArrayList

class GifAdapter(val mainActivity: MainActivity) : RecyclerView.Adapter<GifAdapter.GifViewHolder>(), View.OnClickListener {

    lateinit var glide: GlideWrapper


    var gifs: ArrayList<GifObject> = ArrayList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class GifViewHolder(
        val binding: ItemGifBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemGifBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)


        return GifViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        val gif = gifs[position]
        with(holder.binding) {
            holder.itemView.tag = gif
            gifTextView.tag = gif
            gifTextView.text = gif.getDesription()

            glide.setContAndView(gifImageView.context, gifImageView)
            checkGifAndPost(gif)
        }
    }

    override fun getItemCount(): Int = gifs.size

    override fun onClick(v: View?) {
        //("Not yet implemented")
    }


    private fun checkGifAndPost(obj: GifObject) {
        //Some request does not have .gif URL(API feature)
        //Preview picture is used instead
        var uri: String? = obj.getUri()
        if (uri == null) {
            uri = obj.getPreviewUri()!!
            glide.addBitmap(uri)
        } else {
            glide.addGif(uri)
        }
    }
}