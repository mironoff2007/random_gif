package mironov.random_gif.ui


import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mironov.random_gif.*
import mironov.random_gif.databinding.ActivityMainBinding
import mironov.random_gif.glide.GlideWrapper
import mironov.random_gif.model.GifObject
import mironov.random_gif.model.MainActivityViewModel
import mironov.random_gif.model.Status


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var textView: TextView
    private lateinit var buttonClear: Button


    private var gifObject: GifObject? = null
    private var glide: GlideWrapper = GlideWrapper()

    private lateinit var adapter: GifAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       // Debug.waitForDebugger()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.initRepository(this.applicationContext)
        initViews()
        setupObserver()
        setButtonListeners()

        //Get first gif
        viewModel.getNext()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveToPrefs()
    }

    private fun setButtonListeners() {

        buttonClear.setOnClickListener{
            viewModel.clear()
        }

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                   viewModel.getNext()
                }
            }
        })
    }

    private fun initViews() {
        textView= findViewById(R.id.textView)
        buttonClear = findViewById(R.id.buttonClear)

        adapter= GifAdapter(this)
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
                    textView.text = ""

                    populateRecycler()
                }
                Status.DATA -> {
                    textView.text = ""

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

                    glide.clear()
                }
            }
        }
    }


}