package com.owlylabs.platform.util

import com.owlylabs.platform.helpers.crypto.Base64
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.playerlibrary.helpers.crypto.AESHelper
import java.util.*


class SubscriptionsUtil {
    companion object {
        fun encodeText(text: String): String {
            return Base64.encode(AESHelper.encrypt(text))
        }

        fun decodeText(text: String): String? {
            return AESHelper.decrypt(text)
        }

    }
}