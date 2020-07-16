package com.sean.finalproject

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_date_list.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val listDates = arrayListOf("1965", "1966", "1967", "1968", "1969", "1970", "1971", "1972", "1973", "1974", "1975", "1976", "1977",
        "1978", "1979", "1980", "1981", "1982", "1983", "1984", "1985", "1986", "1987", "1988", "1990", "1991", "1992", "1993", "1994", "1995")

    val baseUrl = "https://archive.org/"

    val okHttpClient : OkHttpClient = OkHttpClient()

    var selectedYearData : JSONArray = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var dateListView : ListView = findViewById(R.id.date_list)

        var arrayList : ArrayList<String> = listDates
        var arrayAdapter : ArrayAdapter<String> = ArrayAdapter(this, android.R.layout.simple_list_item_activated_1, arrayList)
        dateListView.adapter = arrayAdapter

        val context = this
        dateListView.setOnItemClickListener {parent, view, position, id ->
            val year = listDates[position]

            val concertIntent = ConcertListActivity.newIntent(context, year)
            startActivity(concertIntent)
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


    private fun isNetworkConnected() : Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
