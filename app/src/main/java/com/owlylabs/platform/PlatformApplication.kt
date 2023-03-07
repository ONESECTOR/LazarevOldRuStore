package com.owlylabs.platform

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import com.jakewharton.threetenabp.AndroidThreeTen
import com.owlylabs.epublibrary.OwlyReader
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.di.android.DaggerAppComponent
import com.owlylabs.platform.di.java.CommonComponent
import com.owlylabs.platform.di.java.DaggerCommonComponent
import com.owlylabs.platform.util.ApplicationUtil
import com.owlylabs.platform.util.exceptions.NoMetaFoundException
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import com.yandex.metrica.push.YandexMetricaPush
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import zendesk.core.Zendesk
import zendesk.support.Support

class PlatformApplication : DaggerApplication() {

    companion object{
        private lateinit var appInstance: PlatformApplication
        private lateinit var appCommonComponent: CommonComponent

        fun get(): PlatformApplication {
            return appInstance
        }
    }

    init {
        appInstance = this
        appCommonComponent = DaggerCommonComponent.factory().create(
            appInstance,
            ApiConstants.API_BASE_URL
        )
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //Logger.setLoggable(true);

        var currentProcName : String? = null
        val pid = Process.myPid()
        val manager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == pid) {
                currentProcName = processInfo.processName
                break
            }
        }

        currentProcName?.let {
            if (it == BuildConfig.APPLICATION_ID){
                OwlyReader.init(this)
                Zendesk.INSTANCE.init(
                    this,
                    "https://owlylabs.zendesk.com",
                    "5f47927f556ef8c87bbeacd23df92a490b2f8a69e536108b",
                    "mobile_sdk_client_222c19f5ca64dcc2edf7"
                )

                Support.INSTANCE.init(Zendesk.INSTANCE)
                AndroidThreeTen.init(this)
                ProcessLifecycleOwner.get().lifecycle.addObserver(appCommonComponent.billing())
            }
        }

        val config: YandexMetricaConfig
        try {
            config = YandexMetricaConfig
                .newConfigBuilder(ApplicationUtil.getManifestMetaData(applicationContext, getString(R.string.yandex_api_key)))
                .build()
            YandexMetrica.activate(applicationContext, config)
            YandexMetrica.enableActivityAutoTracking(this)
            YandexMetricaPush.init(applicationContext);
        } catch (e: NoMetaFoundException) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(
            appInstance,
            appCommonComponent.retrofit(),
            appCommonComponent.repository(),
            appCommonComponent.billing()
        )
    }
}