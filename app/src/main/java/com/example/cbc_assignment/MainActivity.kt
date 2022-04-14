package com.example.cbc_assignment

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ListView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    var date: String? = null
    var title: String? = null
    var arraylist: ArrayList<HashMap<String, String?>> = ArrayList()
    var listview: ListView? = null
    var str:String?=null
    private val urlLink = "https://www.cbc.ca/aggregate_api/v1/items?lineupSlug=news"

    var imageurl: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listview = findViewById(R.id.list1)

        //Sub thread to run the code
        val subThread = Executors.newSingleThreadExecutor()
        runOnUiThread { }  //for preExecutingtasks such as progressbar

        subThread.execute { //for background task
            val sbuilder = StringBuilder()
            try {
                val URLobj = URL(urlLink)
                val http_link = URLobj.openConnection() as HttpURLConnection

                //converting bufferreader stream to String
                val Breader = BufferedReader(InputStreamReader(http_link.inputStream))
                str = Breader.readLine()
                while (str != null) {
                    sbuilder.append(str)
                    str = null
                }
                var geturlString = sbuilder.toString()

                //extracting json array from that string
                val jsarr = JSONArray(geturlString)

                //extracting all the json objects from json array
                for (i in 0 until jsarr.length()) {
                    val jsonobj = jsarr.getJSONObject(i)
                    date = jsonobj.getString("readablePublishedAt")
                    title = jsonobj.getString("title")

                    //putting attributes of each json object nto hashmap
                    val imgFolderStr = jsonobj.getString("images")
                    val imgJsonObj = JSONObject(imgFolderStr)
                    imageurl = imgJsonObj.getString("square_140")
                    val hmap = HashMap<String, String?>()
                    hmap["title"] = title
                    hmap["date"] = date
                    hmap["image"] = imageurl


                    //assigning hashmap objectsto arraylist
                    arraylist.add(hmap)
                }
            } catch (malformedURLException: MalformedURLException) {
                malformedURLException.printStackTrace()
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            } catch (a: Exception) {
                a.printStackTrace()
            }

        runOnUiThread({  //for postexecution tasks

        })

        //to transfer the control back to main thread, where we will call the custom adapter
        var handler = Handler(Looper.getMainLooper())
        handler.post({
            val customAdapter = CustomAdapter(this@MainActivity, arraylist)
            listview?.setAdapter(customAdapter)
        })
    }
    }
}