package com.owlylabs.platform.services

import android.content.Context
import android.content.Intent
import android.os.Binder
import com.owlylabs.platform.model.data.AudioData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.model.repository.remote.ServerAPI
import dagger.android.DaggerIntentService
import io.reactivex.subjects.ReplaySubject
import javax.inject.Inject

private const val AUDIO_BOOK_ID = "com.owlylabs.platform.services.extra.audioId"
private const val APP_ID = "com.owlylabs.platform.services.extra.appId"


class AudioService : DaggerIntentService("OpenBookService") {

    private val binder = AudioServiceBinder()

    @Inject
    lateinit var apiService: ServerAPI
    @Inject
    lateinit var repository: AbstractLocalRepository

    val subject = ReplaySubject.create<Pair<AudioData, String>>()

    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            it.extras?.let {
                val audioId = it.getInt(AUDIO_BOOK_ID)
                val appId = it.getInt(APP_ID)
                getHash(appId)?.let {
                    val book = repository.getAudioByIdBlocking(audioId)
                    subject.onNext(Pair(book, it))
                }
            }
        }
    }

    private fun getHash(appId: Int): String?{
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
        return hash
    }

    override fun onBind(intent: Intent): AudioServiceBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        return true
    }

    override fun onRebind(intent: Intent?) {

    }

    inner class AudioServiceBinder : Binder() {
        fun getSubject(): ReplaySubject<Pair<AudioData, String>> {
            return subject
        }
    }

    companion object {
        @JvmStatic
        fun openBook(context: Context, bookId: Int, appId: Int) {
            val intent = Intent(context, AudioService::class.java).apply {
                putExtra(AUDIO_BOOK_ID, bookId)
                putExtra(APP_ID, appId)
            }
            context.startService(intent)
        }
    }
}
