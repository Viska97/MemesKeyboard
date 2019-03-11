package org.visapps.vkstickerskeyboard.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.saved_stickers_fragment.*
import org.visapps.vkstickerskeyboard.GlideApp
import org.visapps.vkstickerskeyboard.R
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.ui.adapter.SavedPacksAdapter
import org.visapps.vkstickerskeyboard.ui.viewmodels.SavedStickersViewModel
import org.visapps.vkstickerskeyboard.util.InjectorUtils
import org.visapps.vkstickerskeyboard.util.toVisibility

class SavedStickersFragment : Fragment() {

    companion object {
        fun newInstance() = SavedStickersFragment()
    }

    private lateinit var navController: NavController
    private lateinit var viewModel: SavedStickersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saved_stickers_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
        if (activity is AppCompatActivity){ (activity as AppCompatActivity).setSupportActionBar(toolbar)}
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        val factory = InjectorUtils.provideSavedStickersViewModelFactory(requireActivity())
        viewModel = ViewModelProviders.of(this, factory).get(SavedStickersViewModel::class.java)
        initAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    private fun initAdapter() {
        val glide = GlideApp.with(this)
        val adapter = SavedPacksAdapter(glide)
        packs_list.adapter = adapter
        viewModel.packs.observe(this, Observer<List<Pack>>{
            adapter.updatePacks(it)
            packs_list.visibility = toVisibility(it.isNotEmpty())
            empty_msg.visibility = toVisibility(it.isEmpty())
        })
    }

}
