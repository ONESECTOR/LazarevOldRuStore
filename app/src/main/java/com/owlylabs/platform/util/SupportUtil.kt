package com.owlylabs.platform.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import com.owlylabs.platform.BuildConfig
import com.owlylabs.platform.R
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.account.InterfaceShowEmailDialog
import zendesk.support.CustomField
import zendesk.support.request.RequestActivity
import zendesk.support.requestlist.RequestListActivity


class SupportUtil {
    companion object {
        fun writeToSupport(
            mContext: Context,
            localRepository: AbstractLocalRepository,
            interfaceShowEmailDialog: InterfaceShowEmailDialog
        ) {
            localRepository.getAccountEmailBlocking()?.let {


                val customFieldAppVersion = CustomField(360008963739L, "as")

                val appVers = "v_".plus(BuildConfig.APP_VERSION)

                val requestActivityConfig = RequestActivity.builder()
                    .withRequestSubject(mContext.getString(R.string.zendesk_ticket_subject))
                    .withTags(
                        Build.MANUFACTURER.plus("_").plus(Build.MODEL.replace(" ", "")),
                        "Android_".plus(Build.VERSION.RELEASE),
                        appVers
                    )
                    //.withCustomFields(listOf(customFieldAppVersion))
                    .config()
                mContext.startActivity(
                    RequestListActivity.builder().intent(
                        mContext,
                        listOf(requestActivityConfig)
                    )
                )


            } ?: run {
                interfaceShowEmailDialog.showEmailDialog()
            }

        }

        fun rateThisApp(mContext: Context) {
            try {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=".plus(BuildConfig.APPLICATION_ID))
                    )
                )
            } catch (e1: ActivityNotFoundException) {
                try {
                    InternetUtil.openLink(
                        mContext, "http://play.google.com/store/apps/details?id=".plus(
                            BuildConfig.APPLICATION_ID
                        )
                    )
                } catch (e2: ActivityNotFoundException) {
                    ToastUtil.showText(mContext, "You don't have any app that can open this link")
                }
            }
        }
    }
}