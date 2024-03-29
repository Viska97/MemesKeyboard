package org.visapps.vkmemeskeyboard.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.pack_fragment.*
import kotlinx.android.synthetic.main.pack_fragment.view.*
import org.visapps.vkmemeskeyboard.GlideApp
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.data.models.Meme
import org.visapps.vkmemeskeyboard.ui.adapter.MemesAdapter
import org.visapps.vkmemeskeyboard.ui.viewmodels.PackViewModel
import org.visapps.vkmemeskeyboard.util.InjectorUtil
import org.visapps.vkmemeskeyboard.util.NetworkState
import org.visapps.vkmemeskeyboard.util.calculateNoOfColumns
import org.visapps.vkmemeskeyboard.util.toVisibility

class PackFragment : Fragment() {

    companion object {
        fun newInstance() = PackFragment()
    }

    private lateinit var viewModel: PackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.pack_fragment, container, false)
        view.reload_button.setOnClickListener {
            viewModel.reload()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navController = findNavController()
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).setSupportActionBar(toolbar)
        }
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        val packId = arguments?.getInt("packId") ?: 0
        val factory = InjectorUtil.providePackViewModelFactory(requireActivity(), packId)
        viewModel = ViewModelProviders.of(this, factory).get(PackViewModel::class.java)
        initAdapter()
        initObservers()
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = MemesAdapter(glide)
        stickers_list.layoutManager = GridLayoutManager(requireActivity(),
            calculateNoOfColumns(requireActivity()), RecyclerView.VERTICAL, false)
        stickers_list.adapter = adapter
        viewModel.stickers.observe(this, Observer<List<Meme>> {
            adapter.updateStickers(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun initObservers() {
        viewModel.pack.observe(this, Observer<Pack> {
            toolbar.title = it.name
        })
        viewModel.networkState.observe(this, Observer<NetworkState> {
            progress_bar.visibility =
                toVisibility(it == NetworkState.RUNNING)
            error_msg.visibility =
                toVisibility(it == NetworkState.FAILED)
            reload_button.visibility =
                toVisibility(it == NetworkState.FAILED)
            stickers_list.visibility =
                toVisibility(it == NetworkState.SUCCESS)
        })
    }
}
