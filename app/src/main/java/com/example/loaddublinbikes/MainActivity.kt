package com.example.loaddublinbikes

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.stations_row.view.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerStationList.setBackgroundColor(Color.BLACK)

        val stationList = listOf(

            BikeStation(25, "Street 5", 10),
            BikeStation(26, "Street 2", 12),
            BikeStation(27, "Street 3", 13)

        )

        recyclerStationList.layoutManager = LinearLayoutManager(this)

        // recyclerStationList.adapter = StationListAdapter(stationList)


        getDublinBikesJsonFeed()
    }


    private fun getDublinBikesJsonFeed() {
        var apiKey = "382f37ee8c2fe3ed3ed7185b2f9d63573a827604"
        var url =
            "https://api.jcdecaux.com/vls/v1/stations?contract=dublin&apiKey=" + apiKey

        //Create a request object

        val request = Request.Builder().url(url).build()

        //Create a client

        val client = OkHttpClient()

        //Use client object to work with request object

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // TODO("Not yet implemented")
                Log.i("JSON", "JSON HTTP CALL FAILED")
            }

            override fun onResponse(call: Call, response: Response) {
                // TODO("Not yet implemented")
                Log.i("JSON", "JSON HTTP CALL SUCCEEDED")
                val body = response?.body?.string()
                //  println("json loading" + body)
                //Log.i("JSON", body)

                //body


                var jsonBody = "{\"stations\": " + body + "}"
                Log.i("JSON", jsonBody)

                val gson = GsonBuilder().create()
                var stationList = gson.fromJson(jsonBody, Stations::class.java)

                // Log.i("JSON", stationList.stations[0].name)

                runOnUiThread {
                    recyclerStationList.adapter = StationListAdapter(stationList.stations)

                }
            }
        })

    }

}


class StationListAdapter(val stations: List<BikeStation>)
    :
    RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // TODO("Not yet implemented")
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.stations_row, parent, false)
        return CustomViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // TODO("Not yet implemented")

        holder.itemView.stationName.text = stations[position].name
        holder.itemView.bikesAvailable.text = stations[position].available_bikes.toString()
    }

    override fun getItemCount(): Int {
        ///  TODO("Not yet implemented")

        return stations.size
    }


}


class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {

}
