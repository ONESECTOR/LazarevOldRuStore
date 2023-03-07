package com.owlylabs.platform.util

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.*
import android.os.Build
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
import androidx.core.content.ContextCompat.startActivity


class InternetUtil {
    companion object {
        fun registerNetworkCallback(
            context: Context,
            onInternetAvailable: () -> Unit,
            onInternetLost: () -> Unit
        )
                : ConnectivityManager.NetworkCallback? {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            if (connectivityManager is ConnectivityManager) {
                val networkCallback = object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        onInternetAvailable()
                    }

                    override fun onLost(network: Network) {
                        onInternetLost()
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    connectivityManager.registerDefaultNetworkCallback(networkCallback)
                } else {
                    val request = NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build()
                    connectivityManager.registerNetworkCallback(request, networkCallback)
                }
                return networkCallback
            }
            return null
        }

        fun unregisterNetworkCallback(
            context: Context,
            networkCallback: ConnectivityManager.NetworkCallback
        ) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            if (connectivityManager is ConnectivityManager) {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }

        fun openLink(mContext: Context, link: String?) {
            if (!link.isNullOrEmpty()) {
                val builder = CustomTabsIntent.Builder()
                builder.setExitAnimations(mContext, 0, 0)
                //builder.setStartAnimations(requireContext(), R.anim.slide_in_from_right, 0)
                if (isCustomTabSupported(mContext, Uri.parse(link))){
                    builder.build().launchUrl(mContext, Uri.parse(link))
                } else {
                    try {
                        val browserIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        mContext.startActivity(browserIntent)
                    } catch (ex : Exception){

                    }
                }
            }
        }

        fun isCustomTabSupported(
            context: Context,
            url: Uri?
        ): Boolean {
            return getCustomTabsPackages(context, url).size > 0
        }

        fun getCustomTabsPackages(context: Context, url: Uri?): ArrayList<ResolveInfo> {
            val pm = context.packageManager
            // Get default VIEW intent handler.
            val activityIntent = Intent(Intent.ACTION_VIEW, url)

            // Get all apps that can handle VIEW intents.
            val resolvedActivityList =
                pm.queryIntentActivities(activityIntent, 0)
            val packagesSupportingCustomTabs = ArrayList<ResolveInfo>()
            for (info in resolvedActivityList) {
                val serviceIntent = Intent()
                serviceIntent.action = ACTION_CUSTOM_TABS_CONNECTION
                serviceIntent.setPackage(info.activityInfo.packageName)
                // Check if this package also resolves the Custom Tabs service.
                if (pm.resolveService(serviceIntent, 0) != null) {
                    packagesSupportingCustomTabs.add(info)
                }
            }
            return packagesSupportingCustomTabs
        }

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            if (cm is ConnectivityManager) {

                if (Build.VERSION.SDK_INT < 23) {
                    val ni = cm.activeNetworkInfo
                    if (ni != null) {
                        return ni.isConnected && (ni.type == ConnectivityManager.TYPE_WIFI || ni.type == ConnectivityManager.TYPE_MOBILE)
                    }
                } else {
                    val n = cm.activeNetwork
                    if (n != null) {
                        val nc = cm.getNetworkCapabilities(n)
                        if (nc != null) {
                            return nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(
                                NetworkCapabilities.TRANSPORT_WIFI
                            )
                        }
                    }
                }
            }

            return false
        }
    }
}