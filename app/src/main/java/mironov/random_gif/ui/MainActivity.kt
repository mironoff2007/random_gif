package mironov.random_gif.ui


import android.opengl.Visibility
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import mironov.random_gif.*
import mironov.random_gif.databinding.ActivityMainBinding
import mironov.random_gif.glide.GlideWrapper
import mironov.random_gif.model.GifObject
import mironov.random_gif.model.MainActivityViewModel
import mironov.random_gif.model.Status


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var progressBar: ProgressBar

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
        setListeners()

        //Get gifs from cache
        populateRecycler()
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveToPrefs()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear -> {
                viewModel.clear()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setListeners() {
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
        adapter= GifAdapter(this)
        adapter.glide=glide

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter

        binding.progressBar.visibility= View.INVISIBLE
    }

    private fun populateRecycler() {
        adapter.gifs= viewModel.getGifsListFromCache()
    }

    private fun setupObserver() {
        viewModel.mutableStatus.observe(this) { status ->
            when (status) {
                Status.DATA -> {
                    binding.progressBar.visibility= View.INVISIBLE

                    populateRecycler()
                }
                Status.LOADING -> {
                    binding.progressBar.visibility= View.VISIBLE

                }
                Status.ERROR -> {
                    binding.progressBar.visibility= View.INVISIBLE
                   Toast.makeText(applicationContext,getString(R.string.error_message), Toast.LENGTH_LONG).show()
                }
                Status.CLEARCAHCE -> {
                    binding.progressBar.visibility= View.INVISIBLE
                    //Toast.makeText(applicationContext,getString(R.string.cleared_cache), Toast.LENGTH_LONG).show()
                    populateRecycler()
                    glide.clear()
                }
            }
        }
    }
}