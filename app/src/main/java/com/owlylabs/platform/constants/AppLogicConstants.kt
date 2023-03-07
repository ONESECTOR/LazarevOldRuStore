package com.owlylabs.platform.constants

import androidx.annotation.Keep

class AppLogicConstants {
    @Keep
    companion object {
        val BRAND_URL by lazyOf("https://owlylabs.com/" )  // link to base url of api
        val TAG_NEED_RELOAD_DATA by lazy { "TAG_NEED_RELOAD_DATA" }
        const val BINDING_ADAPTER_NEWS_IMG = "binging_adapter_news_item"
        const val BINDING_ADAPTER_ENABLE_NAV_BAR_PADDING = "app:binding_adapter_enable_nav_bar_padding"
        const val BINDING_ADAPTER_SELECTED_SOMU_SUB = "binging_adapter_selected_some_sub"

        const val ARG_TAB_ID = "tabId"

        val TAG_IS_EMAIL_DIALOG_SUCCESS by lazy { "TAG_IS_EMAIL_DIALOG_SUCCESS" }

        val RECYCLERVIEW_VELOCITY by lazy { 0.6f }
    }

    enum class TabViewHolderType{
        TITLE,
        SUBTITLE,
        NEWS_ITEM,
        SEPARATOR,
        BOOK,
        BOOK_HORIZONTAL_SECTION,
        AUDIO,
        AUDIO_HORIZONTAL_SECTION,
        VIDEO,
        VIDEO_HORIZONTAL_SECTION,
        BANNER_HORIZONTAL_SECTION
    }

    enum class BookCollectionViewHolderType{
        BOOK,
        SUBTITLE
    }

    enum class AudioCollectionViewHolderType{
        AUDIO,
        SUBTITLE
    }

    enum class AccountListHolderType{
        NAME_HOLDER,
        ACTION_HOLDER,
        BANNER_HOLDER,
        FOOTER_HOLDER
    }

    enum class StartScreenSubscriptionHolderType{
        HEADER_HOLDER,
        ACTION_HOLDER,
        SUBSCRIPTION_HOLDER,
        FOOTER_HOLDER
    }
}