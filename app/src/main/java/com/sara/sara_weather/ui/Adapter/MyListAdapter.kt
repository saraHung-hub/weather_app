package com.sara.sara_weather.ui.Adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.sara.sara_weather.R
import com.sara.sara_weather.ui.Constant

class MyListAdapter(private val context: Activity, private val type: List<String>, private val item: List<String>, private val item2: List<String>, private val timeList: List<String>)
    : ArrayAdapter<String>(context, R.layout.custom_list, type) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_list, null, true)

        val titleText = rowView.findViewById(R.id.type) as TextView
        val subtitleText = rowView.findViewById(R.id.item) as TextView
        val time = rowView.findViewById(R.id.time) as TextView
        val removeBtn = rowView.findViewById(R.id.remove) as Button

        titleText.text = type[position]
        if (titleText.text.toString().equals("GPS", ignoreCase = true)){
            subtitleText.text = context.getString(R.string.title_lat) + " " + item[position] + ", " + context.getString(R.string.title_lon) + " " + item2[position]
        }
        else{
            subtitleText.text = item[position]
        }

        time.text = context.getString(R.string.search_time) + " " + timeList[position]

        removeBtn.setTag(position) //important so we know which item to delete on button click


        removeBtn.setOnClickListener(View.OnClickListener { v ->

            val positionToRemove = v.tag as Int //get the position of the view to delete stored in the tag
            val typeList = Constant.type as ArrayList<String>
            val itemList = Constant.item as ArrayList<String>
            val itemList2 = Constant.item2 as ArrayList<String>
            val time = Constant.time as ArrayList<String>

            typeList.removeAt(positionToRemove)
            itemList.removeAt(positionToRemove)
            itemList2.removeAt(positionToRemove)
            time.removeAt(positionToRemove)

            Constant.type = typeList
            Constant.item = itemList
            Constant.item2 = itemList2
            Constant.time = time
            notifyDataSetChanged()
        })

        return rowView
    }
}