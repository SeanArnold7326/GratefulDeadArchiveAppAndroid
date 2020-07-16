package com.sean.finalproject

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SongAdapter(private val context: Context, private val dataSource: ArrayList<SongFile>) : BaseAdapter() {

    private val inflater : LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.song_list_item, parent, false)

        val song_text = rowView.findViewById(R.id.song_title) as TextView

        val songFile = getItem(position) as SongFile
        
        song_text.text = songFile.title

        return rowView
    }
}