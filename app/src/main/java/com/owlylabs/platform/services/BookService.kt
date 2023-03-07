package com.owlylabs.platform.services

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.FileObserver
import androidx.lifecycle.MutableLiveData
import com.owlylabs.platform.helpers.crypto.Base64
import com.owlylabs.platform.model.data.BookData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.model.repository.remote.ServerAPI
import com.owlylabs.platform.util.FileUtil
import com.owlylabs.playerlibrary.helpers.crypto.AESHelper
import dagger.android.DaggerIntentService
import io.reactivex.subjects.ReplaySubject
import okio.Okio
import okio.buffer
import okio.sink
import java.io.File
import java.util.ArrayList
import javax.inject.Inject

private const val BOOK_ID = "com.owlylabs.platform.services.extra.bookId"
private const val APP_ID = "com.owlylabs.platform.services.extra.appId"


class BookService : DaggerIntentService("OpenBookService") {

    private val binder = BookServiceBinder()

    private lateinit var fileObserver: CustomFileObserver

    @Inject
    lateinit var apiService: ServerAPI
    @Inject
    lateinit var repository: AbstractLocalRepository

    val subject = ReplaySubject.create<BookData>()

    val listOfCurrentlyDownloadingBookIds = MutableLiveData<ArrayList<Int>>(ArrayList())

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            it.extras?.let {
                val bookId = it.getInt(BOOK_ID)
                val appId = it.getInt(APP_ID)
                listOfCurrentlyDownloadingBookIds.value?.let {listOfCurrentlyDownloadingBookIdsValue ->
                    if (!listOfCurrentlyDownloadingBookIdsValue.contains(bookId)){
                        val file = File(FileUtil.getBookFilePath(this, bookId))
                        val fileDir = File(FileUtil.getBookFolderFilePath(this, bookId))
                        if (!fileDir.exists()) {
                            fileDir.mkdirs()
                        }
                        if (!file.exists()) {
                            listOfCurrentlyDownloadingBookIdsValue.add(bookId)
                            listOfCurrentlyDownloadingBookIds.postValue(listOfCurrentlyDownloadingBookIdsValue)
                            file.createNewFile()
                            var hash: String? = null
                            repository.getHashBlocking()?.let {
                                hash = it.hash
                            }
                            if (hash == null) {
                                var udid: String? = null
                                repository.getUdidBlocking()?.let {
                                    udid = it.udid
                                }
                                if (udid == null) {
                                    apiService.getUdidBlocking().execute().body()?.let {
                                        repository.saveUdidBlocking(it)
                                        udid = it.udid
                                    }
                                }
                                udid?.let {
                                    apiService.registerUserBlocking(appId, it).execute().body()?.let {
                                        repository.saveHashBlocking(it)
                                        hash = it.hash
                                    }
                                }
                            }

                            hash?.let {
                                apiService.getServerTimestampBlocking().execute().body()?.let {
                                    val timestamp = it.timestamp
                                    val key = timestamp.toString().plus("_").plus(bookId)
                                    val encryptResult: ByteArray? =
                                        AESHelper.encrypt(key)
                                    val keyAes128: String = Base64.encode(encryptResult)
                                    val keyAes128Chars = keyAes128.toCharArray()
                                    val resultKeyAes128 = StringBuilder()
                                    for (symbol in keyAes128Chars) {
                                        if (AESHelper.isSpecialCharacter(symbol)) {
                                            resultKeyAes128.append('%')
                                            resultKeyAes128.append(AESHelper.charToHex(symbol))
                                            //resultKeyAes128.append(symbol)
                                        } else {
                                            resultKeyAes128.append(symbol)
                                        }
                                    }
                                    val finalRequestString = resultKeyAes128.toString()
                                    val call = apiService.downloadBookBlocking(finalRequestString).execute()
                                    call.body()?.let {
                                        fileObserver = CustomFileObserver(
                                            file.absolutePath,
                                            FileObserver.CLOSE_WRITE,
                                            bookId
                                        )
                                        fileObserver.startWatching()

                                        val sink = file.sink().buffer()
                                        sink.writeAll(it.source())
                                        sink.flush()
                                        sink.close()
                                    }
                                }

                            }
                        } else {
                            subject.onNext(repository.getBookByIdBlocking(bookId))
                        }
                    }
                }

            }
        }
    }

    inner class CustomFileObserver(val filePath: String, mask: Int, val bookId: Int) :
        FileObserver(filePath, mask) {
        override fun onEvent(event: Int, path: String?) {
            if (event == FileObserver.CLOSE_WRITE) {
                val book = repository.getBookByIdBlocking(bookId)
                listOfCurrentlyDownloadingBookIds.value?.let {
                    if (it.contains(bookId)){
                        it.remove(bookId)
                        listOfCurrentlyDownloadingBookIds.postValue(it)
                    }
                }
                subject.onNext(book)
            }
        }

    }

    override fun onBind(intent: Intent): BookServiceBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    override fun onRebind(intent: Intent?) {

    }

    inner class BookServiceBinder : Binder() {
        fun getSubject(): ReplaySubject<BookData> {
            return subject
        }
        fun getListOfCurrentlyDownloadingBookIds() : MutableLiveData<ArrayList<Int>>{
            return listOfCurrentlyDownloadingBookIds
        }
    }

    companion object {
        @JvmStatic
        fun openBook(context: Context, bookId: Int, appId: Int) {
            val intent = Intent(context, BookService::class.java).apply {
                putExtra(BOOK_ID, bookId)
                putExtra(APP_ID, appId)
            }
            context.startService(intent)
        }
    }
}
