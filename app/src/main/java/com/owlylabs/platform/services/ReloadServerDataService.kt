package com.owlylabs.platform.services

import android.content.Context
import android.content.Intent
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.model.repository.remote.ServerAPI
import dagger.android.DaggerIntentService
import javax.inject.Inject

private const val ACTION_LOAD_RECOMMENDED = "com.owlylabs.platform.services.action.load_recommended"
private const val ACTION_FULL_RELOAD = "com.owlylabs.platform.services.action.full_reload"
private const val IS_MANUAL_RELOAD = "com.owlylabs.platform.services.extra.is_manual_reload"

class ReloadServerDataService : DaggerIntentService("ReloadServerDataService") {

    @Inject lateinit var apiService: ServerAPI
    @Inject lateinit var repository: AbstractLocalRepository

    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FULL_RELOAD -> {
                val isManualLoad = intent.getBooleanExtra(IS_MANUAL_RELOAD, false)
                handleActionFullReload(isManualLoad)
            }
            ACTION_LOAD_RECOMMENDED -> {
                handleActionRecommendedReload()
            }
        }
    }

    private fun checkDoWeNeedReloadStructure(): Boolean {
        return true
    }

    private fun checkDoWeNeedReloadContent(): Boolean {
        return true
    }

    private fun checkDoWeNeedReloadRecommended(): Boolean {
        return true
    }

    private fun handleActionFullReload(isManualLoad: Boolean) {
        if (isManualLoad || checkDoWeNeedReloadStructure()){
            val response = apiService.downloadStructureBlocking(ApiConstants.API_PARAM_CONST_VERSION).execute()
            if (response.isSuccessful) {
                response.body()?.let { structureCallback ->
                    repository.reInitAllRemoteStructure(structureCallback)
                }
            }
        }
        if (isManualLoad || checkDoWeNeedReloadContent()){
            val currentTimestamp = repository.getContentTimestampBlocking()
            val response = apiService.downloadContentBlocking(ApiConstants.API_PARAM_CONST_APP_ID, currentTimestamp).execute()
            if (response.isSuccessful) {
                response.body()?.let { contentCallback ->
                    repository.reInitAllRemoteContent(contentCallback)
                }
            }
        }
    }

    private fun handleActionRecommendedReload() {
        if (checkDoWeNeedReloadRecommended()){
            val response = apiService.getRecommendedAppsBlocking(
                ApiConstants.API_PARAM_CONST_DATE,
                ApiConstants.API_PARAM_CONST_DEVICE,
                ApiConstants.API_PARAM_CONST_APP_ID
            ).execute()
            if (response.isSuccessful) {
                response.body()?.let { recommendedCallback ->
                    repository.reInitAllRecommendedBanners(recommendedCallback)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun startActionFullDownload(context: Context, isManualLoad: Boolean) {
            val intent = Intent(context, ReloadServerDataService::class.java).apply {
                action = ACTION_FULL_RELOAD
                putExtra(IS_MANUAL_RELOAD, isManualLoad)
            }
            context.startService(intent)
        }
        @JvmStatic
        fun startReloadRecommended(context: Context) {
            val intent = Intent(context, ReloadServerDataService::class.java)
            intent.action = ACTION_LOAD_RECOMMENDED
            context.startService(intent)
        }
    }
}
