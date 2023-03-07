package com.owlylabs.platform.ui.activity_start_screen.enter_name

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import com.owlylabs.platform.databinding.FragmentStartScreenNameBinding
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.activity_main.MainActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class StartScreenNameFragment : DaggerFragment() {
    @Inject
    lateinit var localTabsRepository: AbstractLocalRepository

    private lateinit var fragmentViewBinding : FragmentStartScreenNameBinding
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentViewBinding = FragmentStartScreenNameBinding.inflate(inflater, container, false)
        return fragmentViewBinding.root
    }

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        fragmentViewBinding.editTextName.addTextChangedListener {
            it?.let {
                fragmentViewBinding.buttonSubscribe.isEnabled = it.isNotEmpty()
            } ?: kotlin.run {
                fragmentViewBinding.buttonSubscribe.isEnabled = false
            }
        }
        fragmentViewBinding.buttonSubscribe.setOnClickListener {
            fragmentViewBinding.editTextName.text?.let {
                if (it.isNotEmpty()){
                    hideKeyboard()
                    localTabsRepository.saveAccountName(it.toString())
                    val sharedPreference = context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                    var editor = sharedPreference?.edit()
                    editor?.putBoolean("first_launch", true)
                    editor?.commit()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    activity?.finish()
                }
            }
        }
        fragmentViewBinding.buttonSkip.setOnClickListener {
            val sharedPreference = context?.getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference?.edit()
            editor?.putBoolean("first_launch", false)
            editor?.commit()
            startActivity(Intent(requireContext(), MainActivity::class.java))
            activity?.finish()
        }
    }

    private fun hideKeyboard(){
        val imm =
            mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(fragmentViewBinding.editTextName.getWindowToken(), 0)
    }
}
