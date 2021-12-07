package mironov.random_gif.ui

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import mironov.random_gif.databinding.ItemGifBinding

class GifAdapter : RecyclerView.Adapter<GifAdapter.GifViewHolder>(), View.OnClickListener {

    var gifs: List<String> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class GifViewHolder(
        val binding: ItemGifBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: GifViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int = gifs.size

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}