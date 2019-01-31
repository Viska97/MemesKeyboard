package org.visapps.vkstickerskeyboard.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.visapps.vkstickerskeyboard.R

class AllStickersFragment : Fragment() {

    companion object {
        fun newInstance() = AllStickersFragment()
    }

    private lateinit var viewModel: AllStickersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.all_stickers_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AllStickersViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("Vasily", "State of all stickers saved")
        super.onSaveInstanceState(outState)
    }
}
