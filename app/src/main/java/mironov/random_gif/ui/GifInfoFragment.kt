package mironov.random_gif.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mironov.random_gif.databinding.FragmentGifInfoBinding
import mironov.random_gif.glide.GlideWrapper
import mironov.random_gif.model.GifObject

class GifInfoFragment : Fragment() {

    private var gif: GifObject? = null

    private lateinit var binding: FragmentGifInfoBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentGifInfoBinding.inflate(inflater, container, false).apply {
        gif = arguments?.getParcelable(GIF_OBJECT)
            ?: throw IllegalArgumentException("Can't get GIF_OBJECT")
        binding = FragmentGifInfoBinding.inflate(inflater, container, false)

        gifTextView.text = gif?.getDesription()

        if (gif != null) {
            GlideWrapper(requireContext()).checkGifAndPost(gif!!,gifImageView)
        }

    }.root

    companion object {
        @JvmStatic
        private val GIF_OBJECT = "GIF_OBJECT"

        fun newInstance(gif: GifObject): GifInfoFragment {
            val args = Bundle()
            args.putParcelable(GIF_OBJECT, gif)
            val fragment = GifInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}