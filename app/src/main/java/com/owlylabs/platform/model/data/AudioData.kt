package com.owlylabs.platform.model.data

data class AudioData (
    var idBook: String,
    var nameBook: String,
    var description: String,
    var audioSize: String,
    var audioDuration: String,
    var urlImg: String,
    var urlLargeImg: String,
    var bookType: String,
    var inAppPKeyAudio: String
) : Comparable<AudioData>{
    override fun compareTo(other: AudioData): Int {
        if (this.idBook != other.idBook) return -1
        if (this.nameBook != other.nameBook) return -1
        if (this.description != other.description) return -1
        if (this.audioSize != other.audioSize) return -1
        if (this.audioDuration != other.audioDuration) return -1
        if (this.urlImg != other.urlImg) return -1
        if (this.urlLargeImg != other.urlLargeImg) return -1
        if (this.bookType != other.bookType) return -1
        if (this.inAppPKeyAudio != other.inAppPKeyAudio) return -1
        return 0
    }

}