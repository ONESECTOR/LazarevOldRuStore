package com.owlylabs.platform.util

import android.content.Context
import android.content.Intent
import com.owlylabs.platform.R

class ShareUtil {
    companion object{
        fun shareWithDescriptionContent(context: Context, contentName: String){
            return shareText(context,
                context.getString(
                    R.string.placeholder_share_with_content,
                    contentName
                )
            )
        }

        fun recommendOurApp(context: Context){
            return shareText(context, context.getString(R.string.recommend_our_app))
        }

        private fun shareText(context: Context, text: String){
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            context.startActivity(shareIntent)
        }
    }
}