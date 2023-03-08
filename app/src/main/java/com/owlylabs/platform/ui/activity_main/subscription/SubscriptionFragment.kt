package com.owlylabs.platform.ui.activity_main.subscription

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import com.owlylabs.platform.constants.BillingConstants
import com.owlylabs.platform.databinding.FragmentSubscriptionBinding
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.account.InterfaceShowEmailDialog
import com.owlylabs.platform.ui.activity_main.MainActivity
import com.owlylabs.platform.ui.activity_main.subscription.adapter.SubscriptionAdapter
import com.owlylabs.platform.util.SupportUtil
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SubscriptionFragment : DaggerFragment(), InterfaceShowEmailDialog {

    private var _binding: FragmentSubscriptionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SubscriptionViewModel

    private lateinit var adapter: SubscriptionAdapter

    @Inject
    lateinit var repository: AbstractLocalRepository

    private val subscriptions = listOf(
        Pair("Месячная подписка", "459 рублей в месяц"),
        Pair("Годовая подписка", "3 590 рублей в месяц")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            owner = this,
            factory = SubscriptionViewModel.Factory()
        )[SubscriptionViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscriptionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onMainActivity()
            }
        })

        binding.btnBack.setOnClickListener {
            onMainActivity()
        }
        binding.btnTermsOfUse.setOnClickListener {
            onOpenTermsOfUse(BillingConstants.TERMS_OF_USE_URL)
        }
        binding.btnSupport.setOnClickListener {
            onOpenSupport()
        }

        adapter = SubscriptionAdapter(
            subscriptions,
            onItemClick = {
                Log.d("click_test", it.first)
            }
        )
        binding.subscriptions.itemAnimator = null
        binding.subscriptions.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onMainActivity() {
        startActivity(
            Intent(requireContext(), MainActivity::class.java)
        )
        activity?.finish()
    }

    private fun openBrowserByUrl(url: String) {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        )
    }

    private fun onOpenSupport() {
        SupportUtil.writeToSupport(requireContext(), repository, this)
    }

    private fun onOpenTermsOfUse(url: String) {
        try {
            openBrowserByUrl(url)
        } catch (_: ActivityNotFoundException) {

        }
    }

    override fun showEmailDialog() {

    }

}