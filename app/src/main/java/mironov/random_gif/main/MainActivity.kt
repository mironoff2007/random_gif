package mironov.random_gif.main


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import mironov.random_gif.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var buttonPrev: Button
    private lateinit var buttonNext: Button
    private lateinit var buttonClear: Button

    private lateinit var glide: GlideWrapper
    private var gifObject: GifObject? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.initRepository(this.applicationContext)
        initViews()
        setupObserver()
        setButtonListeners()

        //Init Glide. Glide code is moved to wrapper
        glide = GlideWrapper()
        glide.setContAndView(imageView.context, imageView)

        //Lock prev button on create

        //Lock prev button on create
        buttonPrev.isEnabled = false
        //Get first gif
        viewModel.getNext()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveToPrefs()
    }

    private fun setButtonListeners() {
        buttonNext.setOnClickListener { viewModel.getNext() }
        buttonPrev.setOnClickListener {  viewModel.getPrev() }
        buttonClear.setOnClickListener{
            viewModel.clear()
            glide.clear()
        }
    }

    private fun initViews() {
        imageView = findViewById(R.id.imageView)
        textView = findViewById(R.id.textView)
        buttonPrev = findViewById(R.id.buttonPrev)
        buttonNext = findViewById(R.id.buttonNext)
        buttonClear = findViewById(R.id.buttonClear)
    }

    private fun setupObserver() {
        viewModel.mutableStatus.observe(this) { status ->
            when (status) {
                Status.DATAFIRST -> {
                    gifObject = viewModel.getGifObject()
                    checkGifAndPost(gifObject!!)
                    textView.setText(gifObject!!.getDesription())
                    //Lock prev button
                    buttonPrev.isEnabled = false
                    buttonPrev.background =
                        baseContext.getDrawable(R.drawable.button_background_inactive)
                }
                Status.DATA -> {
                    gifObject = viewModel.getGifObject()
                    gifObject?.let { checkGifAndPost(it) }
                    textView.setText(gifObject!!.getDesription())
                    //Unlock prev button
                    buttonPrev.isEnabled = true
                    buttonPrev.background = baseContext.getDrawable(R.drawable.button_background)
                }
                Status.LOADING -> {
                    textView.text = getString(R.string.loading)
                }
                Status.ERROR -> {
                    textView.text = getString(R.string.error_message)
                    //Show error placeholder
                    glide.addGif("")
                }
                Status.CLEARCAHCE -> {
                    textView.text = baseContext.getString(R.string.cleared_cache)
                    //Lock prev button
                    buttonPrev.isEnabled = false
                    buttonPrev.background = baseContext.getDrawable(R.drawable.button_background_inactive)
                }
            }
        }
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