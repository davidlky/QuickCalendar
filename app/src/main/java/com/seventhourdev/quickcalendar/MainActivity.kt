package com.seventhourdev.quickcalendar

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.seventhourdev.quickcalendar.adapters.CalendarListAdapter
import com.seventhourdev.quickcalendar.models.Event
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmQuery
import java.util.*

class MainActivity : AppCompatActivity() {

    var eventList: ArrayList<Event> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val config = RealmConfiguration.Builder(this).build()
        Realm.setDefaultConfiguration(config)
        setUpListView()
    }

    private fun setUpListView() {
        val realm = Realm.getDefaultInstance()
        val queryResult = realm.where(Event::class.java).findAll().toTypedArray()
        for (event in queryResult){
            event.end.time=Date(event.endLong)
            event.start.time=Date(event.startLong)
            eventList.add(event)
        }
        val listView = findViewById(R.id.listView) as ListView
        val listAdapter = CalendarListAdapter(eventList, this)
        listView.adapter = listAdapter
    }
}
