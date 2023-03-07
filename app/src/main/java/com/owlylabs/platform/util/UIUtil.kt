package com.owlylabs.platform.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.*

import android.os.Build
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition


class UIUtil {
    companion object {
        fun loadImageUri(context: Context, imageUri : Uri, setImage : (Drawable) -> Unit){
            Glide.with(context).load(imageUri).into(object : SimpleTarget<Drawable>() {
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    setImage(resource)
                }
            })
        }
    }
}