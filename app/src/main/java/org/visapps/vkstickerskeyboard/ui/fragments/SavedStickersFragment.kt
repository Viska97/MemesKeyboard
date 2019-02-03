package org.visapps.vkstickerskeyboard.ui.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.saved_stickers_fragment.*

import org.visapps.vkstickerskeyboard.R

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
        viewModel = ViewModelProviders.of(this).get(SavedStickersViewModel::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("Vasily", "State of saved stickers saved")
        super.onSaveInstanceState(outState)
    }

}
