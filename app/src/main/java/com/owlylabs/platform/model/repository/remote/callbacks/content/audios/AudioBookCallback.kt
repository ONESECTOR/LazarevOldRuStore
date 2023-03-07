package com.owlylabs.platform.model.repository.remote.callbacks.content.audios

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class AudioBookCallback (
    @SerializedName("id_book") val idBook : Int,
    @SerializedName("name_book") val nameBook : String,
    @SerializedName("description") val description : String,
    @SerializedName("audio_size") val audioSize : Int,
    @SerializedName("audio_duration") val audioDuration : String,
    @SerializedName("url_img") val urlImg : String,
    @SerializedName("url_large_img") val urlLargeImg : String,
    @SerializedName("book_type") val bookType : Int,
    @SerializedName("in_app_p_key_audio") val inAppPKeyAudio : String
)