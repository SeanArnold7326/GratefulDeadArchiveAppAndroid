package com.sean.finalproject

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import java.io.*
import java.lang.Exception
import java.lang.IllegalStateException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URI
import java.net.URL
import javax.xml.transform.URIResolver

class MediaPlayerActivity : AppCompatActivity() {

    var mediaPlayer : MediaPlayer = MediaPlayer()

    var currentSongNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        var concert = intent.extras?.getSerializable("CONCERT") as Concert
        var selectedSong = intent.extras?.getInt("SELECTED_SONG") as Int

        currentSongNumber = selectedSong

        var concertFiles = concert.files

        var selectedSongFile = concertFiles[selectedSong]

        var currentSongLabel = findViewById(R.id.current_song)as TextView
        currentSongLabel.text = "${selectedSongFile.track}   ${selectedSongFile.title}"

        var initialUrl = "https://${concert.d1}/${concert.dir}"

        var initialSongURL = "$initialUrl/${selectedSongFile.fileName}"
        mediaPlayer.setDataSource(this, Uri.parse(initialSongURL))

        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }

        mediaPlayer.prepare()
        mediaPlayer.start()



        val playBtn = findViewById(R.id.play_button) as Button
        playBtn.setOnClickListener {
            if(!mediaPlayer.isPlaying) {
                mediaPlayer.start()
            }
        }

        val pauseBtn = findViewById(R.id.pause_button) as Button
        pauseBtn.setOnClickListener {
            if(mediaPlayer.isPlaying) {
                mediaPlayer.pause()
            }
        }

        val nextBtn = findViewById(R.id.next_button) as Button
        nextBtn.setOnClickListener {
            if(mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            if(currentSongNumber+1 > concertFiles.lastIndex) {
                mediaPlayer.stop()
            } else {
                selectedSongFile = concertFiles[currentSongNumber + 1]
                currentSongNumber++
                currentSongLabel.text = "${selectedSongFile.track}   ${selectedSongFile.title}"
                var songUrl = "$initialUrl/${selectedSongFile.fileName}"
                mediaPlayer.reset()
                mediaPlayer.setDataSource(this, Uri.parse(songUrl))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }

        val prevBtn = findViewById(R.id.previous_button) as Button
        prevBtn.setOnClickListener {
            if(mediaPlayer.isPlaying) {
                mediaPlayer.stop()
            }
            if(currentSongNumber == 0) {
                mediaPlayer.stop()
            } else {
                selectedSongFile = concertFiles[currentSongNumber - 1]
                currentSongNumber--
                currentSongLabel.text = "${selectedSongFile.track}   ${selectedSongFile.title}"
                var songUrl = "$initialUrl/${selectedSongFile.fileName}"
                mediaPlayer.reset()
                mediaPlayer.setDataSource(this, Uri.parse(songUrl))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var dialog : MyDialog = MyDialog()
        dialog.show(supportFragmentManager, "MyDialog")
        return true
    }

    companion object {
        const val CONCERT = ""
        const val SELECTED_SONG = ""

        fun newIntent(context: Context, concert: Concert, selectedSong: Int) : Intent {
            val playerIntent = Intent(context, MediaPlayerActivity::class.java)

            playerIntent.putExtra("CONCERT", concert as Serializable)
            playerIntent.putExtra("SELECTED_SONG", selectedSong)

            return playerIntent
        }
    }
}
