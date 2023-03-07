package com.owlylabs.platform.ui.account

import android.app.Application
import androidx.lifecycle.*
import com.owlylabs.platform.model.data.TabData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.R
import com.owlylabs.platform.model.data.RecommendedData
import com.owlylabs.platform.ui.account.list.data.*


class AccountViewModel(
    repository: AbstractLocalRepository,
    tabId: Int,
    application: Application
) : AndroidViewModel(application) {

    var tabLiveData = MediatorLiveData<TabData>()
    private var hiddenTabLiveData: LiveData<TabData>

    private var hiddenAccountLiveData: LiveData<String>
    private var hiddenRecommendedLiveData:  LiveData<List<RecommendedData>>
    private var hiddenActionsLiveData =  MutableLiveData<List<String>>()
    var listLiveData = MediatorLiveData<ArrayList<AccountFragmentListItemAbstract>>()

    init {
        hiddenTabLiveData = LiveDataReactiveStreams.fromPublisher(repository.getTabFlowable(tabId))
        tabLiveData.addSource(hiddenTabLiveData) { newValue ->
            val oldValue = tabLiveData.value
            var isOldValuePresents = false
            var isOldValueDiffersFromNews = false
            oldValue?.let { oldData ->
                isOldValuePresents = true
                if (oldData != newValue) {
                    isOldValueDiffersFromNews = true
                }
            }
            if ((!isOldValuePresents) || (isOldValuePresents && isOldValueDiffersFromNews)) {
                tabLiveData.value = newValue
            }
        }

        hiddenActionsLiveData.value = application.resources.getStringArray(R.array.account_block).toList()
        hiddenAccountLiveData = LiveDataReactiveStreams.fromPublisher(repository.getAccountNameFlowable())
        hiddenRecommendedLiveData =  LiveDataReactiveStreams.fromPublisher(repository.getRecommended())

        listLiveData.addSource(hiddenAccountLiveData, Observer {
            val arrayListOfData = ArrayList<AccountFragmentListItemAbstract>()
            arrayListOfData.add(
                AccountFragmentListItemName(
                    it
                )
            )
            hiddenActionsLiveData.value?.let {
                it.forEach {
                    arrayListOfData.add(
                        AccountFragmentListItemAction(
                            it
                        )
                    )
                }
            }

            hiddenRecommendedLiveData.value?.let {
                it.forEach {
                    arrayListOfData.add(
                        AccountFragmentListItemBanner(
                            it
                        )
                    )
                }
            }

            arrayListOfData.add(AccountFragmentListItemFooter())
            listLiveData.value = arrayListOfData
        })

        listLiveData.addSource(hiddenRecommendedLiveData, Observer {
            val arrayListOfData = ArrayList<AccountFragmentListItemAbstract>()

            hiddenAccountLiveData.value?.let {
                arrayListOfData.add(
                    AccountFragmentListItemName(
                        it
                    )
                )
            }

            hiddenActionsLiveData.value?.let {
                it.forEach {
                    arrayListOfData.add(
                        AccountFragmentListItemAction(
                            it
                        )
                    )
                }
            }

            it.forEach {
                arrayListOfData.add(
                    AccountFragmentListItemBanner(
                        it
                    )
                )
            }

            arrayListOfData.add(AccountFragmentListItemFooter())
            listLiveData.value = arrayListOfData
        })
    }


    class Factory(
        val repository: AbstractLocalRepository,
        val tabId: Int,
        val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AccountViewModel(
                repository,
                tabId,
                application
            ) as T
        }
    }
}
