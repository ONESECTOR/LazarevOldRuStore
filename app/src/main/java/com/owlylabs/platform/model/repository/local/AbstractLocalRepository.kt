package com.owlylabs.platform.model.repository.local

import com.owlylabs.platform.model.data.*
import com.owlylabs.platform.model.repository.local.db.subs.SubscriptionEntity
import com.owlylabs.platform.model.repository.remote.callbacks.billing.subscription_state.SubStateCallback
import com.owlylabs.platform.model.repository.remote.callbacks.content.ContentCallback
import com.owlylabs.platform.model.repository.remote.callbacks.hash.HashCallback
import com.owlylabs.platform.model.repository.remote.callbacks.recommended.RecommendedAppsResponse
import com.owlylabs.platform.model.repository.remote.callbacks.structure.StructureCallback
import com.owlylabs.platform.model.repository.remote.callbacks.udid.UdidCallback
import io.reactivex.*

abstract class AbstractLocalRepository {

    /*  Fetch Data */
    abstract fun getTabSingle(tabId : Int) : Single<TabData>
    abstract fun getTabFlowable(tabId : Int) : Flowable<TabData>
    abstract fun getAllTabsObservable() : Observable<List<TabData>>
    abstract fun needToKnowIfTabsArePresentObservable() : Observable<Boolean>
    abstract fun needToKnowIfTabsArePresentSingle() : Single<Boolean>

    /* Books */
    abstract fun getBooksBySectionIdBlocking(sectionId: Int): List<BookData>
    abstract fun getBooksBySectionIdFlowable(sectionId: Int): Flowable<List<BookData>>
    abstract fun getBookByIdBlocking(bookId: Int) : BookData
    abstract fun getBookByIdFlowable(bookId: Int) : Flowable<BookData>
    abstract fun getBookNameByIdBlocking(bookId: Int) : Single<String>

    /* Audios */
    abstract fun getAudiosBySectionIdBlocking(sectionId: Int): List<AudioData>
    abstract fun getAudiosBySectionIdFlowable(sectionId: Int) : Flowable<List<AudioData>>
    abstract fun getAudioByIdBlocking(audioBookId: Int) : AudioData
    abstract fun getAudioByIdFlowable(audioBookId: Int) : Flowable<AudioData>
    abstract fun getAudioNameByIdBlocking(audioId: Int) : Single<String>

    /* News */
    abstract fun getNewsBlocking(): List<NewsData>
    abstract fun getNews() : Flowable<List<NewsData>>
    abstract fun getNewsByIdFlowable(newsId: Int) : Flowable<NewsData>

    /* Videos */
    abstract fun getVideosBySectionIdBlocking(sectionId: Int): List<VideoData>
    abstract fun getVideoByIdFlowable(videoId: Int) : Flowable<VideoData>
    abstract fun getVideoNameByVideoIdBlocking(videoId: Int) : Single<String>

    /* Banners */
    abstract fun getBannersBySectionIdBlocking(sectionId: Int): List<BannerData>
    abstract fun getTypeOfBannerAction(id: Int): Maybe<Int>
    abstract fun getSectionByIdMaybe(id: Int): Maybe<SectionData>

    /* Udid & Hash*/
    abstract fun getHashBlocking(): HashData?
    abstract fun getUdidBlocking(): UdidData?
    abstract fun saveHashBlocking(hash: HashCallback)
    abstract fun saveUdidBlocking(udidData: UdidCallback)

    /* Sections */
    abstract fun getSectionsByTabIdFlowable(tabId: Int) : Flowable<List<SectionData>>
    abstract fun getSectionByIdFlowable(sectionId: Int) : Flowable<SectionData>
    abstract fun getSectionByIdBlocking(sectionId: Int) : SectionData

    /* Account */
    abstract fun getAccountNameBlocking() : String
    abstract fun getAccountNameFlowable() : Flowable<String>
    abstract fun saveAccountName(name: String)
    abstract fun getAccountEmailBlocking(): String?
    abstract fun saveAccountEmail(email: String)
    abstract fun getRecommended(): Flowable<List<RecommendedData>>

    abstract fun needToKnowIfTheContentHasEverBeenDownloadedBlocking() : Boolean
    abstract fun needToKnowIfTheContentHasEverBeenDownloadedSingle() : Single<Boolean>
    abstract fun getContentTimestampBlocking() : Long
    abstract fun getContentTimestampObservable() : Observable<Long>
    abstract fun getContentTimestampSingle() : Single<Long>

    /*  Manage Data */
    abstract fun reInitAllRemoteContent(contentCallback: ContentCallback)
    abstract fun reInitAllRemoteStructure(structureCallback: StructureCallback)
    abstract fun reInitAllRecommendedBanners(recommendedCallback: RecommendedAppsResponse)

    /*  Subscription */
    abstract fun saveSubcription(subscription: SubscriptionEntity) : Completable
    abstract fun hasAnyActiveSubscription() : Observable<Boolean>
}