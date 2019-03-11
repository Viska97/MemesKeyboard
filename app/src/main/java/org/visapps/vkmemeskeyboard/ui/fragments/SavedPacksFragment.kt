package org.visapps.vkmemeskeyboard.ui.fragments

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
import kotlinx.android.synthetic.main.saved_packs_fragment.*
import org.visapps.vkmemeskeyboard.GlideApp
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.ui.adapter.SavedPacksAdapter
import org.visapps.vkmemeskeyboard.ui.viewmodels.SavedPacksViewModel
import org.visapps.vkmemeskeyboard.util.InjectorUtils
import org.visapps.vkmemeskeyboard.util.toVisibility

class SavedPacksFragment : Fragment() {

    companion object {
        fun newInstance() = SavedPacksFragment()
    }

    private lateinit var navController: NavController
    private lateinit var viewModel: SavedPacksViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.saved_packs_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
        if (activity is AppCompatActivity){ (activity as AppCompatActivity).setSupportActionBar(toolbar)}
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        val factory = InjectorUtils.provideSavedStickersViewModelFactory(requireActivity())
        viewModel = ViewModelProviders.of(this, factory).get(SavedPacksViewModel::class.java)
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
