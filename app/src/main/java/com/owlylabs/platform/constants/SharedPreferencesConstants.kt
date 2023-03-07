package com.owlylabs.platform.constants

import androidx.annotation.Keep

class SharedPreferencesConstants {
    @Keep
    companion object{
        val NAME by lazy { "name" }
        val EMAIL by lazy { "email" }
        val SUB_DATE by lazy { "sub_date" }
        val FIRST_START by lazy { "FIRST_START" }
    }
}