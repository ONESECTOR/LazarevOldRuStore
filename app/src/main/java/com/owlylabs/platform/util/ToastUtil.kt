package com.owlylabs.platform.util

import android.content.Context
import android.widget.Toast

class ToastUtil {
    companion object{
        fun showError(context: Context, throwable: Throwable){
            showText(context, throwable.toString())
        }

        fun showText(context: Context, text: CharSequence){
            Toast.makeText(
                context,
                text,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}