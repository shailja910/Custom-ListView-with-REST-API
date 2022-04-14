package com.example.cbc_assignment

import android.widget.ArrayAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.util.ArrayList
import java.util.HashMap

class CustomAdapter(var ct: Context, arraylist: ArrayList<*>) : ArrayAdapter<Any>(ct,R.layout.activity_template,arraylist)
{    var al: ArrayList<*>
    var c: Context? =null
    init {
        al = arraylist
        c=ct
    }

    override fun getCount(): Int {
        return al.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val inflat = ct.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        convertView = inflat.inflate(R.layout.activity_template, null)
        val tv1 = convertView.findViewById<TextView>(R.id.tv1)
        val tv2 = convertView.findViewById<TextView>(R.id.tv2)
         for (i in al.indices) {
            var map: HashMap<String?, String?>
            map = al[position] as HashMap<String?, String?>
            val getTitle = map["title"]
            val getDate = map["date"]
            tv1.text = getTitle
            tv2.text = getDate
        }
        return convertView
    }
}