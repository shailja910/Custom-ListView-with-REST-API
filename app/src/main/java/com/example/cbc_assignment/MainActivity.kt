package com.example.cbc_assignment

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.appcompat.widget.SearchView
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
    var str: String? = null
    private val urlLink = "https://www.cbc.ca/aggregate_api/v1/items?lineupSlug=news"

    //to pick image from URL
    var imageurl: String? = null

    //filter on the basis of type
    var type: String? = null

    //receiver registration
    var MyReceiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listview = findViewById(R.id.list1)

        MyReceiver = MyReceiver()
        registerReceiver(MyReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))


        //A. To show url data on custom listview
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

                    type = jsonobj.getString("type")
                    val hmap = HashMap<String, String?>()
                    hmap["title"] = title
                    hmap["date"] = date
                    hmap["image"] = imageurl
                    hmap["type"] = type

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

            runOnUiThread {  //for postexecution tasks
            }

            //to transfer the control back to main thread, where we will call the custom adapter
            var handler = Handler(Looper.getMainLooper())
            handler.post {
                val customAdapter = CustomAdapter(this@MainActivity, arraylist)
                listview?.setAdapter(customAdapter)
            }
        }
        var status = ""
    }


    //B. to implement search operation on the basis of type
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu.findItem(R.id.search_id) //from menu file
        val sv = search.actionView as SearchView
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val obj = ArrayList<HashMap<String, String>>()
                for (x in arraylist) {
                    if (x["type"]!!.contains(newText)) {
                        obj.add(x as HashMap<String, String>)
                    }
                }
                val p = listview!!.adapter as CustomAdapter
                p.update(obj)
                return false
            }
        }) //search closed
        return true
    }


    //C. to check internet connectivity
    companion object {
        @androidx.annotation.RequiresPermission(value = "android.permission.ACCESS_NETWORK_STATE")
        fun isConnected(c: Context): String? {
            var status: String? = null
            val cm = c.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                    status = "Wifi enabled"
                } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                    status = "Mobile data enabled"
                }
            } else {
                status = "No internet is available"
            }
            return status
        }
    }
}