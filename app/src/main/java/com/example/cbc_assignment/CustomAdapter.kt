package com.example.cbc_assignment

import android.widget.ArrayAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
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
        var cv = convertView
        val inflat = ct.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        cv = inflat.inflate(R.layout.activity_template, null)
        val tv1 = cv.findViewById<TextView>(R.id.tv1)
        val tv2 = cv.findViewById<TextView>(R.id.tv2)
        val iv = cv.findViewById<ImageView>(R.id.iv)
        for (i in al.indices) {
            var map: HashMap<String?, String?>
            map = al[position]as HashMap<String?, String?>
            val getTitle = map["title"]
            val getDate = map["date"]
            val getImage = map["image"]
            Glide.with(ct)
                .load(getImage)
                .into(iv)
            tv1.text = getTitle
            tv2.text = getDate
        }
        return cv
    }

    fun update(obj: ArrayList<HashMap<String, String>>) {
        al = ArrayList<Any?>()
        (al as ArrayList<Any?>).addAll(obj!!)
        notifyDataSetChanged()
    }
}