package com.tokersoftware.todokt.fragment.settings

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PurchasesUpdatedListener
import com.tokersoftware.todokt.R
import com.tokersoftware.todokt.databinding.FragmentHomeBinding
import com.tokersoftware.todokt.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    lateinit var _binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater,container,false);
        return _binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Get SharedPreferences
        val sharedPref = requireContext().getSharedPreferences(getString(R.string.sharedpreferences_key), Context.MODE_PRIVATE)
        _binding.switchNotification.isChecked = sharedPref.getBoolean(getString(R.string.sharedpreferences_notification), true)

        _binding.switchNotification.setOnCheckedChangeListener { i, isChecked ->
            val editor = sharedPref.edit()
            editor.putBoolean(getString(R.string.sharedpreferences_notification), isChecked)
            editor.apply()
        }

    }

}