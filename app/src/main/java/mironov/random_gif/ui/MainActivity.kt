package mironov.random_gif.ui


import android.os.Bundle
import android.os.Debug
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import mironov.random_gif.*
import mironov.random_gif.databinding.ActivityMainBinding
import mironov.random_gif.glide.GlideWrapper
import mironov.random_gif.model.GifObject
import mironov.random_gif.model.MainActivityViewModel
import mironov.random_gif.model.Status


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var textView: TextView
    private lateinit var buttonPrev: Button
    private lateinit var buttonNext: Button
    private lateinit var buttonClear: Button


    private var gifObject: GifObject? = null
    private var glide: GlideWrapper = GlideWrapper()

    private lateinit var adapter: GifAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Debug.waitForDebugger()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.initRepository(this.applicationContext)
        initViews()
        setupObserver()
        setButtonListeners()

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
        }
    }

    private fun initViews() {
        textView= findViewById(R.id.textView)
        buttonPrev = findViewById(R.id.buttonPrev)
        buttonNext = findViewById(R.id.buttonNext)
        buttonClear = findViewById(R.id.buttonClear)

        adapter= GifAdapter()
        adapter.glide=glide

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

    }

    private fun populateRecycler() {
        adapter.gifs= viewModel.getGifsListFromCache()
    }


    private fun setupObserver() {
        viewModel.mutableStatus.observe(this) { status ->
            when (status) {
                Status.DATAFIRST -> {
                    textView.text =""
                    //Lock prev button
                    buttonPrev.isEnabled = false
                    buttonPrev.background =
                        baseContext.getDrawable(R.drawable.button_background_inactive)

                    populateRecycler()
                }
                Status.DATA -> {
                    textView.text =""
                    //Unlock prev button
                    buttonPrev.isEnabled = true
                    buttonPrev.background = baseContext.getDrawable(R.drawable.button_background)

                    populateRecycler()
                }
                Status.LOADING -> {
                    textView.text = getString(R.string.loading)
                }
                Status.ERROR -> {
                    textView.text = getString(R.string.error_message)
                }
                Status.CLEARCAHCE -> {
                    textView.text = baseContext.getString(R.string.cleared_cache)
                    //Lock prev button
                    buttonPrev.isEnabled = false
                    buttonPrev.background = baseContext.getDrawable(R.drawable.button_background_inactive)
                    glide.clear()
                }
            }
        }
    }


}