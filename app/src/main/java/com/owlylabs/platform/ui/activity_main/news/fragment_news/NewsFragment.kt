package com.owlylabs.platform.ui.news.fragment_news

import com.owlylabs.platform.ui.activity_main.tab.TabThatRepresentsSectionsFragment

class NewsFragment : TabThatRepresentsSectionsFragment() {}

/*class NewsFragment : DaggerFragment(), NewsRecyclerViewAdapter.INavToNewsDetail {
    private lateinit var viewModel: NewsViewModel
    private lateinit var parentRecyclerView: RecyclerView
    private lateinit var parentRecyclerViewLayoutManager: GridLayoutManager
    private var tabId: Int by Delegates.notNull()
    @Inject
    lateinit var repository: AbstractLocalRepository
    private var newsAdapter =
        NewsRecyclerViewAdapter(this)
    private lateinit var mContext: Context
    private lateinit var mNewsItemDecoration: NewsRecyclerViewitemDecoration
    private var isNeedFullReloadRecyclerView = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            tabId = args.getInt(AppLogicConstants.ARG_TAB_ID)
        }
        mNewsItemDecoration =
            NewsRecyclerViewitemDecoration(
                mContext
            )
        parentRecyclerViewLayoutManager =
            GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        viewModel = ViewModelProvider(this, NewsViewModel.Factory(repository, tabId)).get(
            NewsViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentRecyclerView = view.findViewById(R.id.recycler_view_parent_news)
        parentRecyclerView.setPadding(
            parentRecyclerView.paddingStart,
            parentRecyclerView.paddingTop + FrameworkUtil.getToolbarHeight(mContext),
            parentRecyclerView.paddingEnd,
            parentRecyclerView.paddingBottom + FrameworkUtil.getBottomNavigationViewHeight(mContext)
        )
        *//*parentRecyclerViewLayoutManager.spanSizeLookup =
    object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return when (position) {
                0 -> 2
                else -> 1
            }
        }
    }*//*
        if (parentRecyclerView.itemDecorationCount == 0) {
            parentRecyclerView.addItemDecoration(mNewsItemDecoration)
        }
        if (parentRecyclerView.layoutManager == null) {
            parentRecyclerView.layoutManager = parentRecyclerViewLayoutManager
        }
        if (parentRecyclerView.adapter == null) {
            parentRecyclerView.adapter = newsAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.sectionData.observe(this, Observer { sectionData ->
            isNeedFullReloadRecyclerView = true
            newsAdapter.sectionData = sectionData
            //parentRecyclerView.recycledViewPool.clear()
            if (viewModel.newsLiveData.hasObservers()) {
                viewModel.newsLiveData.removeObservers(this)
            }
            viewModel.newsLiveData.observe(this, Observer { newsList ->
                newsAdapter.updateData(newsList, isNeedFullReloadRecyclerView)
                isNeedFullReloadRecyclerView = false
            })
        })
    }

    override fun onStop() {
        super.onStop()
        viewModel.sectionData.removeObservers(this)
        viewModel.newsLiveData.removeObservers(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parentRecyclerView.layoutManager = null
    }

    override fun navToNewsDetail(detailId: Int) {
        val action =
            NewsFragmentDirections
                .actionNewsFragmentToNewsDetailFragment(detailId)
        findNavController().navigate(action)
        //ToastUtil.showText(mContext, detailId.toString())
    }
}*/