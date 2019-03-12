package org.visapps.vkmemeskeyboard.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import kotlinx.android.synthetic.main.all_packs_fragment.*
import org.visapps.vkmemeskeyboard.GlideApp
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.ui.adapter.PacksAdapter
import org.visapps.vkmemeskeyboard.ui.viewmodels.AllPacksViewModel
import org.visapps.vkmemeskeyboard.util.InjectorUtils
import org.visapps.vkmemeskeyboard.util.NetworkState

class AllPacksFragment : Fragment() {

    companion object {
        fun newInstance() = AllPacksFragment()
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            updateSearchText(newText)
            return true
        }
    }

    private lateinit var navController: NavController
    private lateinit var viewModel: AllPacksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_packs_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
        if (activity is AppCompatActivity){ (activity as AppCompatActivity).setSupportActionBar(toolbar)}
        setHasOptionsMenu(true)
        val factory = InjectorUtils.provideAllStickersViewModelFactory(requireActivity())
        viewModel = ViewModelProviders.of(this, factory).get(AllPacksViewModel::class.java)
        initAdapter()
        initSwipeToRefresh()
        viewModel.showPacks("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_packs_menu, menu)
        val searchView = menu.findItem(R.id.searchBar)?.actionView as SearchView?
        searchView?.queryHint = getString(R.string.search_packs)
        searchView?.setOnQueryTextListener(queryTextListener)
        searchView?.isIconified = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = PacksAdapter(glide) {
            viewModel.loadPacks()
        }
        packs_list.adapter = adapter
        packs_list.itemAnimator = null
        viewModel.packs.observe(this, Observer<PagedList<Pack>> {
            adapter.submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun initSwipeToRefresh() {
        viewModel.refreshState.observe(this, Observer {
            swipe_refresh.isRefreshing = it == NetworkState.RUNNING
        })
        swipe_refresh.setOnRefreshListener {
            packs_list.scrollToPosition(0)
            viewModel.refreshPacks()
        }
    }

    private fun updateSearchText(newText : String?) {
        newText?.let {
            if (viewModel.showPacks(it)) {
                packs_list.scrollToPosition(0)
                (packs_list.adapter as? PacksAdapter)?.submitList(null)
            }
        }
    }

}
