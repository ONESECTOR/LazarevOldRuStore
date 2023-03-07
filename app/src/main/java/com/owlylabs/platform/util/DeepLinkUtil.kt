package com.owlylabs.platform.util

import android.net.Uri

class DeepLinkUtil {
    companion object{
        fun createNewsDetailDeepLink(newsId: Int): Uri{
            return createNewsDetailDeepLink(newsId, 0)
        }
        fun createNewsDetailDeepLink(newsId: Int, doNotWaitMenuAnimation: Int): Uri{
            return Uri.parse("platformapp://main/news/details/?newsId=".plus(newsId).plus("&doNotWaitMenuAnimation=".plus(doNotWaitMenuAnimation)))
        }
        fun createVideoDetailDeepLink(newsId: Int): Uri{
            return Uri.parse("platformapp://main/videos/details/?videoId=".plus(newsId))
        }
        fun createBookDetailDeepLink(bookId: Int): Uri{
            return Uri.parse("platformapp://main/books/details/?bookId=".plus(bookId))
        }
        fun createAudioDetailDeepLink(audioId: Int): Uri{
            return Uri.parse("platformapp://main/audios/details/?audioId=".plus(audioId))
        }
        fun createBookCollectionlDeepLink(sectionId: Int): Uri{
            return Uri.parse("platformapp://main/books/collection/?sectionId=".plus(sectionId))
        }
        fun createAudioCollectionlDeepLink(sectionId: Int): Uri{
            return Uri.parse("platformapp://main/audios/collection/?sectionId=".plus(sectionId))
        }
    }
}