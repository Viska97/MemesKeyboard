package org.visapps.vkstickerskeyboard.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import kotlinx.android.synthetic.main.all_stickers_fragment.*
import org.visapps.vkstickerskeyboard.GlideApp

import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.ui.adapter.PacksAdapter
import org.visapps.vkstickerskeyboard.ui.viewmodels.AllStickersViewModel
import org.visapps.vkstickerskeyboard.util.InjectorUtils
import org.visapps.vkstickerskeyboard.util.NetworkState

class AllStickersFragment : Fragment() {

    companion object {
        fun newInstance() = AllStickersFragment()
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
    private lateinit var viewModel: AllStickersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_stickers_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
        if (activity is AppCompatActivity){ (activity as AppCompatActivity).setSupportActionBar(toolbar)}
        setHasOptionsMenu(true)
        val factory = InjectorUtils.provideAllStickersViewModelFactory(requireActivity())
        viewModel = ViewModelProviders.of(this, factory).get(AllStickersViewModel::class.java)
        initAdapter()
        initSwipeToRefresh()
        viewModel.showPacks("")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_stickers_menu, menu)
        val searchView = menu.findItem(R.id.searchBar)?.actionView as SearchView?
        searchView?.queryHint = getString(R.string.searchpacks)
        searchView?.setOnQueryTextListener(queryTextListener)
        searchView?.isIconified = true
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = PacksAdapter(glide) {
            viewModel.reload()
        }
        packs_list.adapter = adapter
        viewModel.posts.observe(this, Observer<PagedList<Pack>> {
            Log.i("Vasily", "changed")
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
            (packs_list.adapter as? PacksAdapter)?.submitList(null)
            viewModel.refresh()
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
