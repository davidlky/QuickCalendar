package com.seventhourdev.quickcalendar

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.ListView
import android.widget.Toast

import com.seventhourdev.quickcalendar.adapters.CalendarListAdapter
import com.seventhourdev.quickcalendar.models.Event

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    var eventList: ArrayList<Event> = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpListView()
    }

    private fun setUpListView() {
        eventList = ArrayList<Event>()

        val listView = findViewById(R.id.listView) as ListView
        val listAdapter = CalendarListAdapter(eventList, this)
        listView!!.adapter = listAdapter
    }
}
