package org.visapps.vkstickerskeyboard.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.pack_fragment.*
import org.visapps.vkstickerskeyboard.GlideApp

import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.util.InjectorUtils
import org.visapps.vkstickerskeyboard.util.NetworkState
import org.visapps.vkstickerskeyboard.util.toVisibility

class PackFragment : Fragment() {

    companion object {
        fun newInstance() = PackFragment()
    }

    private lateinit var viewModel: PackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pack_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val navController = findNavController()
        if (activity is AppCompatActivity){ (activity as AppCompatActivity).setSupportActionBar(toolbar)}
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        val packId = arguments?.getInt("") ?: 0
        val factory = InjectorUtils.providePackViewModelFactory(requireActivity(), packId)
        viewModel = ViewModelProviders.of(this, factory).get(PackViewModel::class.java)
        reload_button.setOnClickListener { viewModel.reload() }
        initAdapter()
        initObservers()
    }

    private fun initAdapter(){
        val glide = GlideApp.with(this)
    }

    private fun initObservers(){
        viewModel.pack.observe(this, Observer<Pack> {
            toolbar.title = it.name
        })
        viewModel.networkState.observe(this, Observer<Int> {
            progress_bar.visibility = toVisibility(it == NetworkState.RUNNING)
            error_msg.visibility = toVisibility(it == NetworkState.FAILED)
            reload_button.visibility = toVisibility(it == NetworkState.FAILED)
        })
    }

}