package com.owlylabs.platform.ui.activity_start_screen.subscription

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.SkuDetails
import com.owlylabs.platform.billing.BillingClientManager
import com.owlylabs.platform.R
import com.owlylabs.platform.databinding.FragmentStartScreenSubscriptionBinding
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.account.InterfaceShowEmailDialog
import com.owlylabs.platform.ui.activity_main.MainActivity
import com.owlylabs.platform.ui.activity_start_screen.subscription.list.StartScreenSubscriptionRecyclerViewAdapter
import com.owlylabs.platform.ui.activity_start_screen.subscription.list.StartScreenSubscriptionRecyclerViewListener
import com.owlylabs.platform.ui.activity_start_screen.subscription.list.data.*
import com.owlylabs.platform.ui.tab_with_sections.StartScreenSubscriptionRecyclerViewItemDecorations
import com.owlylabs.platform.util.SupportUtil
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class StartScreenSubscriptionFragment : DaggerFragment(), StartScreenSubscriptionRecyclerViewListener, InterfaceShowEmailDialog {

    @Inject
    lateinit var localTabsRepository: AbstractLocalRepository

    @Inject
    lateinit var billingClientManager: BillingClientManager

    private lateinit var mContext: Context

    private lateinit var compositeDisposable: CompositeDisposable

    private lateinit var recyclerViewAdapter : StartScreenSubscriptionRecyclerViewAdapter

    private lateinit var binding : FragmentStartScreenSubscriptionBinding

    private lateinit var viewModel: StartScreenSubscriptionFragmentViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[StartScreenSubscriptionFragmentViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartScreenSubscriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onMainActivity()
            }
        })
        configViews()
        compositeDisposable = CompositeDisposable()
        billingClientManager.skusWithSkuDetails.observe(viewLifecycleOwner) { skus ->
            val data = ArrayList<StartScreenSubscriptionRecyclerViewItemAbstract>()
            data.add(StartScreenSubscriptionRecyclerViewItemHeader())
            resources.getStringArray(R.array.subscription_opportunities_block).forEach {
                data.add(StartScreenSubscriptionRecyclerViewItemAction(it))
            }
            skus.forEach {
                data.add(
                    StartScreenSubscriptionRecyclerViewItemSubscription(
                        it,
                        skus.indexOf(it) == 0
                    )
                )
            }
            data.add(StartScreenSubscriptionRecyclerViewItemFooter())
            recyclerViewAdapter.updateData(data)
        }
        viewModel.selectedSubscriptionLiveData.observe(viewLifecycleOwner) {
            recyclerViewAdapter.updateSelectedState(it)
        }
    }

    private fun configViews() {
        configRecyclerView()
    }

    private fun configRecyclerView() {
        recyclerViewAdapter = StartScreenSubscriptionRecyclerViewAdapter(this, viewModel)
        binding.recyclerView.layoutManager = LinearLayoutManager(mContext, RecyclerView.VERTICAL, false)
        binding.recyclerView.addItemDecoration(StartScreenSubscriptionRecyclerViewItemDecorations(mContext))
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.adapter = recyclerViewAdapter
    }

    override fun onClickRestartBilling() {
        billingClientManager.restart()
    }

    override fun onClickSelectSubscription(skudetails: SkuDetails) {
        viewModel.selectedSubscriptionLiveData.value = skudetails
    }

    override fun onClickTermsOfUsage(url: String) {
        try {
            openBrowserByUrl(url)
        } catch (_: ActivityNotFoundException) {

        }
    }

    override fun onClickSupport() {
        SupportUtil.writeToSupport(mContext, localTabsRepository, this)
    }

    override fun onClickSubscribe(skuDetails: SkuDetails) {
        val skus = billingClientManager.skusWithSkuDetails.value
        skus?.let{
            for(sku in it){
                if (sku.sku == skuDetails.sku){
                    val params = BillingFlowParams.newBuilder().setSkuDetails(sku).build()
                    billingClientManager.launchBillingFlow(requireActivity(), params)
                    break
                }
            }
        }

    }

    override fun showEmailDialog() {
        val action = StartScreenSubscriptionFragmentDirections.actionStartScreenSubscriptionFragmentToEmailDialog()
        findNavController().navigate(action)
    }

    override fun onClickBack() {
        onMainActivity()
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
}
