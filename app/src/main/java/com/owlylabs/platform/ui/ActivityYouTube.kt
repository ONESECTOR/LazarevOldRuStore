package com.owlylabs.platform.ui

import android.graphics.Color
import android.net.Uri
import com.google.android.exoplayer2.ui.PlayerView
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.owlylabs.platform.R

class ActivityYouTube : AppCompatActivity() {
    private lateinit var playerView: PlayerView
    private var player : SimpleExoPlayer? = null

    private var currentPosition : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.BLACK

        if (intent == null) {
            finish()
        } else {
            setContentView(R.layout.activity_video_youtube)
            playerView = findViewById(R.id.video_view)
            playerView.setShowNextButton(false)
            playerView.setShowPreviousButton(false)
        }
    }

    private fun initializePlayer() {
        if (player == null) {

            player = SimpleExoPlayer.Builder(this).build().also {
                playerView.player = it

                it.addListener(object : Player.EventListener{
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        playerView.keepScreenOn = isPlaying
                    }
                })

                val link = intent.getStringExtra("url")

                val mediaItem = MediaItem.fromUri(Uri.parse(link))
                it.setMediaItem(mediaItem)

                it.playWhenReady = true

                currentPosition?.let { pos ->
                    it.seekTo(pos)
                }

                it.prepare()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun releasePlayer() {
        player?.let {
            currentPosition = it.currentPosition
            //currentWindow = player.getCurrentWindowIndex()
            //playWhenReady = player.getPlayWhenReady()
            it.release()
            player = null
        }
    }
}