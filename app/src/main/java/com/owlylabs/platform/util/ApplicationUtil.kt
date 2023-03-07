package com.owlylabs.platform.util

import android.content.Context
import android.content.pm.PackageManager
import com.owlylabs.platform.util.exceptions.NoMetaFoundException

class ApplicationUtil {
    companion object{
        @Throws(NoMetaFoundException::class, PackageManager.NameNotFoundException::class)
        fun getManifestMetaData(context: Context, metaKey: String): String {
            val pm: PackageManager =
                context.packageManager
            val ai = pm.getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
            val data = ai.metaData.getString(metaKey)
            return data ?: throw NoMetaFoundException(metaKey)
        }
    }
}