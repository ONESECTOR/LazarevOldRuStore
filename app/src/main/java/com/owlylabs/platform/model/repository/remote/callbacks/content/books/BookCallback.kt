package com.owlylabs.platform.model.repository.remote.callbacks.content.books

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class BookCallback (
    @SerializedName("id_book")
    val idBook : String,
    @SerializedName("name_book")
    val nameBook : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("count_page")
    val countPage : String,
    @SerializedName("url_img")
    val urlImg : String,
    @SerializedName("url_large_img")
    val urlLargeImg : String,
    @SerializedName("book_type")
    val bookType : String,
    @SerializedName("in_app_p_key")
    val inAppPKey : String
)