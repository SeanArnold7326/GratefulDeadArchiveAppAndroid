package com.sean.finalproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import java.io.Serializable

class SongListActivity : AppCompatActivity() {

    var songListView : ListView? = null

    val context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        songListView = findViewById(R.id.song_list)
        var concert = intent.extras?.getSerializable("CONCERT") as Concert
        var concertFiles = concert.files

        var arrayAdapter = SongAdapter(this@SongListActivity, concertFiles)
        songListView?.adapter = arrayAdapter

        songListView?.setOnItemClickListener { parent, view, position, id ->
            val playerIntent = MediaPlayerActivity.newIntent(context, concert, position)
            startActivity(playerIntent)
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
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

        fun newIntent(context: Context, concert: Concert) : Intent {
            val songIntent = Intent(context, SongListActivity::class.java)

            songIntent.putExtra("CONCERT", concert as Serializable)

            return songIntent
        }
    }
}
