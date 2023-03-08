package com.owlylabs.platform.ui.activity_main.tab

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import com.owlylabs.platform.R
import com.owlylabs.platform.databinding.FragmentTabThatRepresentsSectionsBinding
import com.owlylabs.platform.model.data.BannerData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.tab_with_sections.*
import com.owlylabs.platform.util.DeepLinkUtil
import com.owlylabs.platform.util.InternetUtil
import com.owlylabs.platform.util.insetPadding
import com.owlylabs.platform.constants.AppLogicConstants
import com.owlylabs.platform.ui.audios.fragment_audios.AudiosFragmentDirections
import com.owlylabs.platform.ui.books.fragment_books.BooksFragmentDirections
import com.owlylabs.platform.ui.videos.fragment_videos.VideosFragmentDirections
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject
import kotlin.properties.Delegates

open class TabThatRepresentsSectionsFragment: DaggerFragment(), InterfaceBannerClick {
    protected var tabId: Int by Delegates.notNull()
    private lateinit var gridLayoutManager: GridLayoutManager
    protected lateinit var viewModel: TabThatRepresentsSectionsViewModel

    protected lateinit var recyclerViewAdapter: TabThatRepresentsSectionsRecyclerViewAdapter

    private var gridLayoutManagerState: Parcelable? = null

    private lateinit var INavToFragmentByDeepLink: INavToFragmentByDeepLink

    private lateinit var compositeDisposable: CompositeDisposable

    private var _binding: FragmentTabThatRepresentsSectionsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var repository: AbstractLocalRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is INavToFragmentByDeepLink){
            INavToFragmentByDeepLink = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            tabId = args.getInt(AppLogicConstants.ARG_TAB_ID)
        }
        viewModel = ViewModelProvider(
            this,
            TabThatRepresentsSectionsViewModel.Factory(
                repository,
                tabId
            )
        ).get(tabId.toString(), TabThatRepresentsSectionsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabThatRepresentsSectionsBinding.inflate(
            inflater,
            container,
            false
        )
        compositeDisposable = CompositeDisposable()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configRecyclerView()

        binding.recyclerView.insetPadding()

        when(tabId) {
            1 -> {
                viewModel.tabLiveData.observe(viewLifecycleOwner) { tabData ->
                    binding.materialToolbar.title = tabData.title
                }

                viewModel.listLiveData.observe(viewLifecycleOwner) {
                    recyclerViewAdapter.updateData(it)
                }
            }
            2, 3, 4 -> {
                compositeDisposable.add(
                    repository.hasAnyActiveSubscription().observeOn(AndroidSchedulers.mainThread()).subscribe {
                        if (!it) {
                            when(tabId) {
                                2 -> {
                                    findNavController().navigate(
                                        BooksFragmentDirections.onSubscription()
                                    )
                                }
                                3 -> {
                                    findNavController().navigate(
                                        AudiosFragmentDirections.onSubscription()
                                    )
                                }
                                4 -> {
                                    findNavController().navigate(
                                        VideosFragmentDirections.onSubscription()
                                    )
                                }
                            }
                        } else {
                            viewModel.tabLiveData.observe(viewLifecycleOwner) { tabData ->
                                binding.materialToolbar.title = tabData.title
                            }

                            viewModel.listLiveData.observe(viewLifecycleOwner) {
                                recyclerViewAdapter.updateData(it)
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onStop() {
        compositeDisposable.dispose()
        super.onStop()
    }

    override fun onDestroyView() {
        gridLayoutManagerState = gridLayoutManager.onSaveInstanceState()
        super.onDestroyView()
    }

    private fun configRecyclerView() {
        recyclerViewAdapter = TabThatRepresentsSectionsRecyclerViewAdapter(
                INavToFragmentByDeepLink,
                this
            )

        gridLayoutManager = GridLayoutManager(
            requireContext(),
            resources.getInteger(R.integer.numberOfMaxPossibleElements)
        )

        gridLayoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (recyclerViewAdapter.getItemViewType(position)) {
                    AppLogicConstants.TabViewHolderType.BOOK.ordinal,
                    AppLogicConstants.TabViewHolderType.AUDIO.ordinal -> resources.getInteger(R.integer.midBannerSize)

                    AppLogicConstants.TabViewHolderType.TITLE.ordinal,
                    AppLogicConstants.TabViewHolderType.SUBTITLE.ordinal,
                    AppLogicConstants.TabViewHolderType.SEPARATOR.ordinal,
                    AppLogicConstants.TabViewHolderType.BOOK_HORIZONTAL_SECTION.ordinal,
                    AppLogicConstants.TabViewHolderType.AUDIO_HORIZONTAL_SECTION.ordinal,
                    AppLogicConstants.TabViewHolderType.BANNER_HORIZONTAL_SECTION.ordinal -> resources.getInteger(R.integer.numberOfMaxPossibleElements)

                    AppLogicConstants.TabViewHolderType.VIDEO.ordinal,
                    AppLogicConstants.TabViewHolderType.NEWS_ITEM.ordinal -> resources.getInteger(R.integer.bigBannerSize)
                    else -> -1
                }
            }
        }

        gridLayoutManagerState?.let {
            gridLayoutManager.onRestoreInstanceState(gridLayoutManagerState)
        }

        binding.recyclerView.addItemDecoration(
            TabThatRepresentsSectionsRecyclerViewItemDecorations(
                requireContext()
            )
        )

        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.adapter = recyclerViewAdapter
    }

    override fun onBannerClick(bannerData: BannerData) {
        if (bannerData.jump_type.isNotEmpty()){
            when(bannerData.jump_type.toInt()){
                1 -> {
                    compositeDisposable.add(repository
                        .getTypeOfBannerAction(bannerData.jump.toInt())
                        .subscribeBy(
                            onError = {},
                            onSuccess = {
                                when (it){
                                    1 -> INavToFragmentByDeepLink.navigateToFragmentByDeepLink(DeepLinkUtil.createBookDetailDeepLink(bannerData.jump.toInt()))
                                    2 -> INavToFragmentByDeepLink.navigateToFragmentByDeepLink(DeepLinkUtil.createAudioDetailDeepLink(bannerData.jump.toInt()))
                                    3 -> INavToFragmentByDeepLink.navigateToFragmentByDeepLink(DeepLinkUtil.createVideoDetailDeepLink(bannerData.jump.toInt()))
                                }
                            },
                            onComplete = {}
                        )
                    )
                }
                2 -> {
                    InternetUtil.openLink(requireContext(), bannerData.jump)
                }
                3 -> {
                    compositeDisposable.add(repository
                        .getSectionByIdMaybe(bannerData.jump.toInt())
                        .subscribeBy(
                            onError = {},
                            onSuccess = {
                                when(it.sectionType){
                                    "book" -> INavToFragmentByDeepLink.navigateToFragmentByDeepLink(DeepLinkUtil.createBookCollectionlDeepLink(it.id_section))
                                    "audio" -> INavToFragmentByDeepLink.navigateToFragmentByDeepLink(DeepLinkUtil.createAudioCollectionlDeepLink(it.id_section))
                                }
                            },
                            onComplete = {}
                        )
                    )
                }
                5 -> INavToFragmentByDeepLink.navigateToFragmentByDeepLink(DeepLinkUtil.createNewsDetailDeepLink(bannerData.jump.toInt(), 1))
            }
        }

    }
}