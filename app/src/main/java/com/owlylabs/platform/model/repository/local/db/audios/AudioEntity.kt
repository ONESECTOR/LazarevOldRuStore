package com.owlylabs.platform.model.repository.local.db.audios

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AudioEntity (
    @PrimaryKey var idBook: String,
    var nameBook: String,
    var description: String,
    var audioSize: String,
    var audioDuration: String,
    var urlImg: String,
    var urlLargeImg: String,
    var bookType: String,
    var inAppPKeyAudio: String
)