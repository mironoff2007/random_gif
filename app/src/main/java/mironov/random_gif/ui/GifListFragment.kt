package mironov.random_gif.ui


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import mironov.random_gif.databinding.FragmentGifListBinding
import mironov.random_gif.glide.GlideWrapper
import mironov.random_gif.model.MainActivityViewModel
import android.view.MenuInflater
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mironov.random_gif.model.Status


class GifListFragment : Fragment() {

    private lateinit var viewModel: MainActivityViewModel

    private var glide: GlideWrapper = GlideWrapper()

    private lateinit var adapter: GifAdapter
    private lateinit var binding: FragmentGifListBinding

    var errorToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(mironov.random_gif.R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            mironov.random_gif.R.id.action_clear -> {
                viewModel.clear()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGifListBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        viewModel.initRepository(this.requireContext())

        initViews()
        setupObserver()
        setListeners()

        //Get gifs from cache
        populateRecycler()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveToPrefs()
        errorToast?.cancel()
        errorToast = null
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
        adapter = GifAdapter()
        adapter.glide = glide

        val layoutManager = LinearLayoutManager(this.requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(binding.recyclerView.context, DividerItemDecoration.VERTICAL));

        binding.progressBar.visibility = View.INVISIBLE

    }

    private fun populateRecycler() {
        adapter.gifs = viewModel.getGifsListFromCache()
        //show hint to populate recycler by drag
        if (adapter.gifs.isEmpty()) {
            binding.hint.visibility = View.VISIBLE
        } else {
            binding.hint.visibility = View.INVISIBLE
        }
    }

    private fun setupObserver() {
        viewModel.mutableStatus.observe(this.viewLifecycleOwner) { status ->
            when (status) {
                Status.DATA -> {
                    binding.progressBar.visibility = View.INVISIBLE

                    populateRecycler()
                }
                Status.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.hint.visibility = View.INVISIBLE
                }
                Status.ERROR -> {
                    binding.progressBar.visibility = View.INVISIBLE

                    errorToast?.cancel()
                    errorToast = Toast.makeText(
                        this.requireContext(),
                        getString(mironov.random_gif.R.string.error_message),
                        Toast.LENGTH_LONG
                    )
                    errorToast?.show()

                    if (adapter.gifs.isEmpty()) {
                        binding.hint.visibility = View.VISIBLE
                    } else {
                        binding.hint.visibility = View.INVISIBLE
                    }
                }
                Status.CLEARCAHCE -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    //Toast.makeText(applicationContext,getString(R.string.cleared_cache), Toast.LENGTH_LONG).show()
                    populateRecycler()
                    glide.clear()
                }
            }
        }
    }
}