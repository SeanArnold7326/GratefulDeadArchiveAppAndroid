package com.sean.finalproject

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_concerts_list.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.time.Year

class ConcertListActivity : AppCompatActivity() {

    val baseUrl = "https://archive.org/"

    val okHttpClient : OkHttpClient = OkHttpClient()

    var selectedYearData : JSONArray = JSONArray()

    var concertList : ArrayList<Concert> = java.util.ArrayList()

    var concertListView : ListView? = null

    var context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concert_list)

        concertListView = findViewById(R.id.concert_list_view)
        var year = intent.extras?.getString(YEAR)
        loadYearData(year)

        concertListView?.setOnItemClickListener { parent, view, position, id ->
            val songIntent = SongListActivity.newIntent(context, concertList[position])
            startActivity(songIntent)
        }

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)
    }

    private fun loadYearData(year: String?){
        var data : JSONArray = JSONArray()
        if(isNetworkConnected()) {
            runOnUiThread { loading_concerts_progress.visibility = View.VISIBLE }
            val finalUrl = "$baseUrl/advancedsearch.php?q=collection%3AGratefulDead+mediatype%3Aetree+date%3A%5B$year-01-01+TO+$year-12-31%5D&fl%5B%5D=creator&fl%5B%5D=date&fl%5B%5D=description&fl%5B%5D=format&fl%5B%5D=identifier&fl%5B%5D=item_size&fl%5B%5D=mediatype&fl%5B%5D=name&fl%5B%5D=publisher&fl%5B%5D=source&fl%5B%5D=subject&fl%5B%5D=title&fl%5B%5D=type&fl%5B%5D=year&sort%5B%5D=date+asc&sort%5B%5D=avg_rating+desc&sort%5B%5D=&rows=1000&page=1&output=json&callback=callback&save=yes"
            val request: Request = Request.Builder().url(finalUrl).addHeader("Accept", "application/json").build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("FAILURE", e.toString())
                }

                override fun onResponse(call: Call, response: Response){
                    val json = response.body()?.string()
                    var newJson = json?.removeRange(0,9)
                    newJson = newJson?.removeRange(newJson!!.length, newJson!!.length)
                    val jsonObject = JSONObject(newJson!!)
                    data = JSONArray(JSONObject(jsonObject.get("response").toString()).get("docs").toString())
                    Log.i("Length", data.length().toString())
                    selectedYearData = JSONArray(JSONObject(jsonObject.get("response").toString()).get("docs").toString())

                    for(i in 0 until data.length()) {
                        val item = data.getJSONObject(i)
                        val concert : Concert = loadData(item.get("identifier").toString())!!
                        concertList.add(concert)
                    }

                    runOnUiThread(Runnable {
                        loading_concerts_progress.visibility = View.GONE
                        kotlin.run {
                            var arrayAdapter = ConcertAdapter(this@ConcertListActivity, concertList)
                            concertListView?.adapter = arrayAdapter
                        }
                    })
                }
            })
        }
    }



    private fun loadData(identifier: String) : Concert?{
        var concert : Concert? = null
        if(isNetworkConnected()) {

            val finalUrl = "$baseUrl/metadata/$identifier"
            val request: Request = Request.Builder().url(finalUrl).addHeader("Accept", "application/json").build()

            val response : Response = okHttpClient.newCall(request).execute()
            val json = response.body()?.string()
            val jsonObject = JSONObject(json!!)
            val d1 = if(jsonObject.has("d1")) { jsonObject.get("d1").toString() } else { null }
            val d2 = if(jsonObject.has("d2")) { jsonObject.get("d2").toString() } else { null }
            val dir = if(jsonObject.has("dir")) { jsonObject.get("dir").toString() } else { null }
            val files = JSONArray(jsonObject.get("files").toString())
            val concertFiles = ArrayList<SongFile>()

            for (i in 0 until files.length()) {
                val file = files.getJSONObject(i)
                if(file.has("format") && file.get("format").toString() == "VBR MP3") {
                    val name = if(file.has("name")) { file.get("name").toString() } else { null }
                    val source = if(file.has("source")) { file.get("source").toString() } else { null }
                    val title = if(file.has("title")) { file.get("title").toString() } else { null }
                    val track = if(file.has("track")) { file.get("track").toString() } else { null }
                    val length = if(file.has("length")) { file.get("length").toString() } else { null }
                    val format = if(file.has("format")) { file.get("format").toString() } else { null }

                    val newFile = SongFile(name, source, title, track, length, format)
                    concertFiles.add(newFile)
                }
            }
            val data = JSONObject(jsonObject.get("metadata").toString())

            val identifier = if(data.has("identifier")) { data.get("identifier").toString() } else { null }
            val mediatype = if(data.has("mediatype")) { data.get("mediatype").toString() } else { null }
            val subject = if(data.has("subject")) { data.get("subject").toString() } else { null }
            val description = if(data.has("description")) { data.get("description").toString() } else { null }
            val date = if(data.has("date")) { data.get("date").toString() } else { null }
            val source = if(data.has("source")) { data.get("source").toString() } else { null }
            val venue = if(data.has("venue")) { data.get("venue").toString() } else { null }
            val md5s = if(data.has("md5s")) { data.get("md5s").toString() } else { null }

            concert = Concert(identifier, mediatype, subject, description, date, source, venue, md5s, concertFiles, d1, d2, dir)

        }
        return concert!!
    }

    private fun isNetworkConnected() : Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
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
        const val YEAR = ""

        fun newIntent(context: Context, year: String): Intent {
            val concertIntent = Intent(context, ConcertListActivity::class.java)

            concertIntent.putExtra(YEAR, year)

            return concertIntent
        }
    }
}
