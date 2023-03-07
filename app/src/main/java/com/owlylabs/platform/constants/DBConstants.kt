package com.owlylabs.platform.constants

import androidx.annotation.Keep

class DBConstants {
    @Keep
    companion object {
        const val dbVersion = 3
        const val dbName = "Platform2020"

        const val COLUMN_INFO_TAB_ITEM_ACTIVE_PLATFORM = "activePlatform"
        const val COLUMN_INFO_TAB_ITEM_TAB_TYPE = "tabType"
        const val COLUMN_INFO_TAB_ITEM_ID = "itemId"
        private const val ACTIVE_PLATFORM_ANDROID_AND_IOS = 1
        private const val ACTIVE_PLATFORM_ANDROID = 2

        /* TABS QUERIES */
        const val QUERY_ALL_TABS = "SELECT * FROM TabEntity " +
                "WHERE " +
                "$COLUMN_INFO_TAB_ITEM_ACTIVE_PLATFORM = $ACTIVE_PLATFORM_ANDROID_AND_IOS " +
                "OR " +
                "$COLUMN_INFO_TAB_ITEM_ACTIVE_PLATFORM = $ACTIVE_PLATFORM_ANDROID "

        const val QUERY_GET_TAB_BY_ID = "SELECT * FROM TabEntity " +
                "WHERE " +
                "$COLUMN_INFO_TAB_ITEM_ID = ?"

        const val MIGRATION_1_2_p1 =
            "CREATE TABLE IF NOT EXISTS 'SubscriptionEntity' ('product_id' TEXT NOT NULL, expiryTimeMillis TEXT NOT NULL, PRIMARY KEY(product_id))"

        const val MIGRATION_2_3_p1 =
            "CREATE TABLE IF NOT EXISTS 'ProductEntity' ('product_id' TEXT NOT NULL, has_access INTEGER NOT NULL, PRIMARY KEY(product_id))"
        const val MIGRATION_2_3_p2 =
            "CREATE TABLE IF NOT EXISTS 'SkuIndexEntity' ('sku' TEXT NOT NULL, purchaseType TEXT NOT NULL, PRIMARY KEY(sku))"
    }
}