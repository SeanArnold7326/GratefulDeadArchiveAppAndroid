package com.sean.finalproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ConcertAdapter(private val context: Context, private val dataSource: ArrayList<Concert>) : BaseAdapter() {


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
        val rowView = inflater.inflate(R.layout.concert_list_item, parent, false)

        val titleText = rowView.findViewById(R.id.title_label) as TextView

        val subTitle = rowView.findViewById(R.id.venue_text) as TextView

        val sourceText = rowView.findViewById(R.id.source_text) as TextView

        val concert = getItem(position) as Concert

        titleText.text = concert.date
        subTitle.text = concert.venue
        sourceText.text = concert.source


        return rowView
    }
}