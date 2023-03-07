package com.owlylabs.platform.constants

import androidx.annotation.Keep

class BillingConstants {
    @Keep
    companion object{
        val MONTH by lazy { "com.lazarev.app.sbscr1m" }
        val YEAR by lazy { "com.lazarev.app.sbscr1y" }
        const val TERMS_OF_USE_URL = "https://lazarev.ru/policy-mobileapp/"
    }
}