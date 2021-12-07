package mironov.random_gif.ui

import android.view.LayoutInflater
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
            gifTextView.text = gif

            if (true) { //.isNotBlank()
                /*
                Glide.with(photoImageView.context)
                    .load(user.photo)
                    .circleCrop()
                    .placeholder(R.drawable.ic_user_avatar)
                    .error(R.drawable.ic_user_avatar)
                    .into(photoImageView)
                    */
            } else {
                /*
                Glide.with(photoImageView.context).clear(photoImageView)
                photoImageView.setImageResource(R.drawable.ic_user_avatar)
                // you can also use the following code instead of these two lines ^
                // Glide.with(photoImageView.context)
                //        .load(R.drawable.ic_user_avatar)
                //        .into(photoImageView)
                 */
            }
        }
    }

    override fun getItemCount(): Int = gifs.size

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}