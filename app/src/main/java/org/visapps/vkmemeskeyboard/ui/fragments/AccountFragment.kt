package org.visapps.vkmemeskeyboard.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.account_fragment.*
import org.visapps.vkmemeskeyboard.R
import org.visapps.vkmemeskeyboard.ui.viewmodels.AccountViewModel

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var navController: NavController
    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
        if (activity is AppCompatActivity){ (activity as AppCompatActivity).setSupportActionBar(toolbar)}
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("Vasily", "State of account saved")
        super.onSaveInstanceState(outState)
    }


}
