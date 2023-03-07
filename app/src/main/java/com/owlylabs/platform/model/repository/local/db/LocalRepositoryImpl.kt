package com.owlylabs.platform.model.repository.local.db

import androidx.sqlite.db.SimpleSQLiteQuery
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.owlylabs.platform.constants.DBConstants
import com.owlylabs.platform.constants.SharedPreferencesConstants
import com.owlylabs.platform.model.data.*
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.model.repository.local.db.content_timestamp.ContentTimestampEntity
import com.owlylabs.platform.model.repository.local.db.subs.SubscriptionEntity
import com.owlylabs.platform.model.repository.local.db.table_relations.Section_Item
import com.owlylabs.platform.model.repository.local.db.table_relations.Tab_Section
import com.owlylabs.platform.model.repository.remote.callbacks.content.ContentCallback
import com.owlylabs.platform.model.repository.remote.callbacks.content.audios.AudioBookCallback
import com.owlylabs.platform.model.repository.remote.callbacks.content.banners.BannerCallback
import com.owlylabs.platform.model.repository.remote.callbacks.content.books.BookCallback
import com.owlylabs.platform.model.repository.remote.callbacks.content.news.NewsCallback
import com.owlylabs.platform.model.repository.remote.callbacks.content.videos.VideoCallback
import com.owlylabs.platform.model.repository.remote.callbacks.hash.HashCallback
import com.owlylabs.platform.model.repository.remote.callbacks.recommended.RecommendedAppsResponse
import com.owlylabs.platform.model.repository.remote.callbacks.structure.StructureCallback
import com.owlylabs.platform.model.repository.remote.callbacks.structure.tab.TabCallback
import com.owlylabs.platform.model.repository.remote.callbacks.udid.UdidCallback
import com.owlylabs.platform.util.TextUtil
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import zendesk.core.AnonymousIdentity
import zendesk.core.Zendesk

class LocalRepositoryImpl(val roomClientPlatform: RoomClientPlatform, val rxSharedPreferences: RxSharedPreferences) : AbstractLocalRepository() {

    override fun getAllTabsObservable(): Observable<List<TabData>> {
        return roomClientPlatform.tabs().getAllTabsObservable().subscribeOn(Schedulers.io())
    }

    override fun needToKnowIfTabsArePresentObservable(): Observable<Boolean> {
        return getAllTabsObservable().map {
            it.isNotEmpty()
        }
    }

    override fun needToKnowIfTabsArePresentSingle(): Single<Boolean> {
        return roomClientPlatform.tabs().getAllTabsSingle().subscribeOn(Schedulers.io())
            .map { it.isNotEmpty() }
    }

    override fun getSectionsByTabIdFlowable(tabId: Int): Flowable<List<SectionData>> {
        return roomClientPlatform.sections().getSectionsByTabIdFlowable(tabId).subscribeOn(Schedulers.io())
    }

    override fun getBooksBySectionIdBlocking(sectionId: Int): List<BookData> {
        return roomClientPlatform.books().getBooksBySectionIdBlocking(sectionId)
    }

    override fun getBooksBySectionIdFlowable(sectionId: Int): Flowable<List<BookData>> {
        return roomClientPlatform.books().getBooksBySectionIFlowable(sectionId)
    }

    override fun getAudiosBySectionIdBlocking(sectionId: Int): List<AudioData> {
        return roomClientPlatform.audios().getAudiosBySectionIdBlocking(sectionId)
    }

    override fun getAudiosBySectionIdFlowable(sectionId: Int): Flowable<List<AudioData>> {
        return roomClientPlatform.audios().getAudiosBySectionIdFlowable(sectionId)
    }

    override fun getBannersBySectionIdBlocking(sectionId: Int): List<BannerData> {
        return roomClientPlatform.banners().getBannersBySectionIdBlocking(sectionId)
    }

    override fun getTypeOfBannerAction(id: Int): Maybe<Int> {
        return roomClientPlatform.banners().getBannerActionType(id).subscribeOn(Schedulers.io())
    }

    override fun getSectionByIdMaybe(id: Int): Maybe<SectionData> {
        return roomClientPlatform.banners().getSectionByIdMaybe(id).subscribeOn(Schedulers.io())
    }

    override fun getVideosBySectionIdBlocking(sectionId: Int): List<VideoData> {
        return roomClientPlatform.video().getVideosBySectionIdBlocking(sectionId)
    }

    override fun getNewsBlocking(): List<NewsData> {
        return roomClientPlatform.news().getNewsBySectionIdBlocking()
    }

    override fun getVideoByIdFlowable(videoId: Int): Flowable<VideoData> {
        return roomClientPlatform.video().getVideosById(videoId).subscribeOn(Schedulers.io())
    }

    override fun getBookByIdFlowable(bookId: Int): Flowable<BookData> {
        return roomClientPlatform.books().getBookById(bookId).subscribeOn(Schedulers.io())
    }

    override fun getBookByIdBlocking(bookId: Int): BookData {
        return roomClientPlatform.books().getBookByIdBlocking(bookId)
    }

    override fun getAudioByIdFlowable(audioBookId: Int): Flowable<AudioData> {
        return roomClientPlatform.audios().getAudioByIdFlowable(audioBookId).subscribeOn(Schedulers.io())
    }

    override fun getAudioByIdBlocking(audioBookId: Int): AudioData {
        return roomClientPlatform.audios().getAudioByIdBlocking(audioBookId)
    }

    override fun getBookNameByIdBlocking(bookId: Int): Single<String> {
        return roomClientPlatform.books().getBookNameByBookIdSingle(bookId).subscribeOn(Schedulers.io())
    }

    override fun getVideoNameByVideoIdBlocking(videoId: Int): Single<String> {
        return roomClientPlatform.video().getVideoNameByVideoIdSingle(videoId).subscribeOn(Schedulers.io())
    }

    override fun getAudioNameByIdBlocking(audioId: Int): Single<String> {
        return roomClientPlatform.audios().getAudioNameByAudioIdSingle(audioId).subscribeOn(Schedulers.io())
    }

    override fun getHashBlocking(): HashData? {
        return roomClientPlatform.hash().getHashBlocking()
    }

    override fun getUdidBlocking(): UdidData? {
        return roomClientPlatform.udid().getUdidBlocking()
    }

    override fun saveHashBlocking(hash: HashCallback) {
        roomClientPlatform.hash().insertBlocking(hash)
    }

    override fun saveUdidBlocking(udidData: UdidCallback) {
        roomClientPlatform.udid().insertBlocking(udidData)
    }

    override fun getNews(): Flowable<List<NewsData>> {
        return roomClientPlatform.news().getNews().subscribeOn(Schedulers.io())
    }

    override fun getNewsByIdFlowable(newsId: Int): Flowable<NewsData> {
        return roomClientPlatform.news().getNewsByIdFlowable(newsId).subscribeOn(Schedulers.io())
    }

    override fun getSectionByIdFlowable(sectionId: Int): Flowable<SectionData> {
        return roomClientPlatform.sections().getSectionByIdFlowable(sectionId).subscribeOn(Schedulers.io())
    }

    override fun getSectionByIdBlocking(sectionId: Int): SectionData {
        return roomClientPlatform.sections().getSectionByIdBlocking(sectionId)
    }

    override fun getAccountNameBlocking(): String {
        return rxSharedPreferences.getString(SharedPreferencesConstants.NAME).get()
    }

    override fun getAccountNameFlowable(): Flowable<String> {
        return rxSharedPreferences.getString(SharedPreferencesConstants.NAME).asObservable().toFlowable(BackpressureStrategy.LATEST)
        //return roomClientPlatform.account().getAccountName().subscribeOn(Schedulers.io())
    }

    override fun saveAccountName(name: String) {
        val identityBuilder = AnonymousIdentity.Builder().withNameIdentifier(name)
        if (rxSharedPreferences.getString(SharedPreferencesConstants.EMAIL).isSet){
            identityBuilder.withEmailIdentifier(rxSharedPreferences.getString(SharedPreferencesConstants.EMAIL).get())
        }
        Zendesk.INSTANCE.setIdentity(identityBuilder.build())
        return rxSharedPreferences.getString(SharedPreferencesConstants.NAME).set(name)
    }

    override fun getAccountEmailBlocking(): String? {
        if (rxSharedPreferences.getString(SharedPreferencesConstants.EMAIL).isSet){
            return rxSharedPreferences.getString(SharedPreferencesConstants.EMAIL).get()
        }
        return null
    }

    override fun saveAccountEmail(email: String) {
        val identityBuilder = AnonymousIdentity.Builder().withEmailIdentifier(email)
        if (rxSharedPreferences.getString(SharedPreferencesConstants.NAME).isSet){
            identityBuilder.withNameIdentifier(rxSharedPreferences.getString(SharedPreferencesConstants.NAME).get())
        }
        Zendesk.INSTANCE.setIdentity(identityBuilder.build())
        return rxSharedPreferences.getString(SharedPreferencesConstants.EMAIL).set(email)
    }

    override fun getRecommended(): Flowable<List<RecommendedData>> {
        return roomClientPlatform.recommended().getRecommendedFlowable().subscribeOn(Schedulers.io())
    }

    fun updateTabsBlocking(remoteData: List<TabCallback>) {
        return roomClientPlatform.tabs().clearAndInsertAllTabsBlocking(remoteData)
    }

    override fun getTabSingle(tabId: Int): Single<TabData> {
        val simpleSQLiteQuery = SimpleSQLiteQuery(DBConstants.QUERY_GET_TAB_BY_ID, arrayOf(tabId))
        return roomClientPlatform.tabs().getTabByIdSingle(simpleSQLiteQuery).subscribeOn(Schedulers.io())
    }

    override fun getTabFlowable(tabId: Int): Flowable<TabData> {
        val simpleSQLiteQuery = SimpleSQLiteQuery(DBConstants.QUERY_GET_TAB_BY_ID, arrayOf(tabId))
        return roomClientPlatform.tabs().getTabByIdFlowable(simpleSQLiteQuery).subscribeOn(Schedulers.io())
    }

    override fun getContentTimestampBlocking(): Long {
        return roomClientPlatform.contentTimestamp().getLastTimeStampBlocking()
    }

    override fun getContentTimestampObservable(): Observable<Long> {
        return roomClientPlatform.contentTimestamp().getLastTimeStampObservable()
    }

    override fun getContentTimestampSingle(): Single<Long> {
        return roomClientPlatform.contentTimestamp().getLastTimeStampSingle().subscribeOn(Schedulers.io())
    }

    override fun needToKnowIfTheContentHasEverBeenDownloadedBlocking(): Boolean {
        return getContentTimestampBlocking() != 0L
    }

    override fun needToKnowIfTheContentHasEverBeenDownloadedSingle(): Single<Boolean> {
        return getContentTimestampSingle().map { it != 0L }
    }

    fun updateContentTimestampBlocking(timestamp: Long) {
        roomClientPlatform.contentTimestamp().updateBlocking(ContentTimestampEntity(timestamp))
    }

    fun insertOrUpdateBooksBlocking(remoteData: List<BookCallback>) {
        roomClientPlatform.books().insertOrReplaceBlocking(remoteData)
    }

    fun insertOrUpdateAudiosBlocking(remoteData: List<AudioBookCallback>) {
        roomClientPlatform.audios().insertOrReplaceBlocking(remoteData)
    }

    fun insertOrUpdateVideosBlocking(remoteData: List<VideoCallback>) {
        roomClientPlatform.video().insertOrReplaceBlocking(remoteData)
    }

    fun insertOrUpdateNewsBlocking(remoteData: List<NewsCallback>) {
        roomClientPlatform.news().insertOrReplaceBlocking(remoteData)
    }

    fun deleteAudiosBlocking(remoteData: List<AudioBookCallback>) {
        roomClientPlatform.audios().deleteBlocking(remoteData)
    }

    fun deleteBooksBlocking(remoteData: List<BookCallback>) {
        roomClientPlatform.books().deleteBlocking(remoteData)
    }

    fun deleteVideosBlocking(remoteData: List<VideoCallback>) {
        roomClientPlatform.video().deleteBlocking(remoteData)
    }

    fun deleteNewsBlocking(remoteData: List<NewsCallback>) {
        roomClientPlatform.news().deleteBlocking(remoteData)
    }

    fun insertOrUpdateBannersBlocking(remoteData: List<BannerCallback>) {
        roomClientPlatform.banners().insertOrReplaceBlocking(remoteData)
    }

    fun deleteNewsBanners(remoteData: List<BannerCallback>) {
        roomClientPlatform.banners().deleteBlocking(remoteData)
    }

    override fun reInitAllRemoteContent(contentCallback: ContentCallback) {
        roomClientPlatform.runInTransaction {
            contentCallback.books?.let { changesInBooks ->
                changesInBooks.added?.let { addedBooks ->
                    insertOrUpdateBooksBlocking(addedBooks)
                }
                changesInBooks.edition?.let { changedBooks ->
                    insertOrUpdateBooksBlocking(changedBooks)
                }
                changesInBooks.deleted?.let { deletedBooks ->
                    deleteBooksBlocking(deletedBooks)
                }
            }
            contentCallback.audio?.let { changesInAudio ->
                changesInAudio.added?.let { addedAudios ->
                    insertOrUpdateAudiosBlocking(addedAudios)
                }
                changesInAudio.edition?.let { changedAudios ->
                    insertOrUpdateAudiosBlocking(changedAudios)
                }
                changesInAudio.deleted?.let { deletedAudios ->
                    deleteAudiosBlocking(deletedAudios)
                }
            }
            contentCallback.video?.let { changesInVideo ->
                changesInVideo.added?.let { addedVideos ->
                    insertOrUpdateVideosBlocking(addedVideos)
                }
                changesInVideo.edition?.let { changedVides ->
                    insertOrUpdateVideosBlocking(changedVides)
                }
                changesInVideo.deleted?.let { deletedVideos ->
                    deleteVideosBlocking(deletedVideos)
                }
            }
            contentCallback.news?.let { changesInNews ->
                changesInNews.added?.let { addedNews ->
                    insertOrUpdateNewsBlocking(addedNews)
                }
                changesInNews.edition?.let { changedNews ->
                    insertOrUpdateNewsBlocking(changedNews)
                }
                changesInNews.deleted?.let { deletedNews ->
                    deleteNewsBlocking(deletedNews)
                }
            }
            contentCallback.banners?.let { changesInBanners ->
                changesInBanners.added?.let { addedBanners ->
                    insertOrUpdateBannersBlocking(addedBanners)
                }
                changesInBanners.edition?.let { changedBanners ->
                    insertOrUpdateBannersBlocking(changedBanners)
                }
                changesInBanners.deleted?.let { deletedBanners ->
                    deleteNewsBanners(deletedBanners)
                }
            }
            updateContentTimestampBlocking(contentCallback.timestamp)
        }
    }

    fun saveSection(tabData: List<TabCallback>) {
        roomClientPlatform.tab_sections().clearThisTable()
        roomClientPlatform.sections().clearThisTable()
        roomClientPlatform.section_items().clearThisTable()
        tabData.forEach { tab ->
            tab.sections.forEach { section ->
                roomClientPlatform.tab_sections().insertOrReplaceBlocking(Tab_Section(tab.itemId, section.id_section))
                roomClientPlatform.sections().insertOrReplaceBlocking(section)
                section.item_ids?.let { item_Ids ->
                    val itemIds = TextUtil.parseStringByComma(item_Ids)
                    itemIds.map {  itemId ->
                        itemId.toInt()
                    }.forEach { itemId ->
                        roomClientPlatform.section_items().insertOrReplaceBlocking(Section_Item(0, section.id_section, itemId))
                    }
                }
            }
        }
    }

    override fun reInitAllRemoteStructure(structureCallback: StructureCallback) {
        roomClientPlatform.runInTransaction {
            updateTabsBlocking(structureCallback.tabData)
            saveSection(structureCallback.tabData)
        }
    }

    override fun reInitAllRecommendedBanners(recommendedCallback: RecommendedAppsResponse) {
        roomClientPlatform.runInTransaction {
            roomClientPlatform.recommended().clearThisTableBlocking()
            recommendedCallback.added?.let {
                if (recommendedCallback.added.size > 0){
                    roomClientPlatform.recommended().insertOrReplaceBlocking(recommendedCallback.added)
                }
            }
        }
    }

    override fun saveSubcription(subscription: SubscriptionEntity) : Completable {
        return roomClientPlatform.subscriptions().saveSubscription(subscription).subscribeOn(Schedulers.io())
    }

    override fun hasAnyActiveSubscription() : Observable<Boolean> {
        return roomClientPlatform.subscriptions().hasAnyActiveSubscription().subscribeOn(Schedulers.io())
    }
}