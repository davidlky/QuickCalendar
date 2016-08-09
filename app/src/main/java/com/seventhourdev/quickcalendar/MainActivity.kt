package com.seventhourdev.quickcalendar

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.ListView
import com.seventhourdev.quickcalendar.adapters.CalendarListAdapter
import com.seventhourdev.quickcalendar.models.Event
import com.seventhourdev.quickcalendar.tools.CalendarAccessor
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmQuery
import java.util.*

class MainActivity : AppCompatActivity() {

    var eventList: ArrayList<Event> = ArrayList()

    private val PERMISSION_CALLBACK_VALUE = 132
    private val PERMISSIONS_REQUESTED = arrayOf(android.Manifest.permission.READ_CALENDAR,android.Manifest.permission.WRITE_CALENDAR)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar) as Toolbar)
        supportActionBar!!.title = "Quick Calendar"
        val config = RealmConfiguration.Builder(this).build()
        Realm.setDefaultConfiguration(config)
        if(ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUESTED[0]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUESTED[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,PERMISSIONS_REQUESTED,PERMISSION_CALLBACK_VALUE)
        }else{
            setUpListView()
        }
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

        var calAccessor = CalendarAccessor
        calAccessor.context = this
        var a = calAccessor.getAllCalendars()
        for (item in a){
            Log.d(MainActivity::class.java.simpleName,item.accountName)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CALLBACK_VALUE){

            if (grantResults.size > 0 && grantResults[0] === PackageManager.PERMISSION_GRANTED) {
                setUpListView()
            } else {
                Snackbar.make(findViewById(R.id.listView),"Permissions Needed", Snackbar.LENGTH_INDEFINITE).setAction("Retry", {
                    fun onClick(v: View) {
                        ActivityCompat.requestPermissions(this@MainActivity, PERMISSIONS_REQUESTED, PERMISSION_CALLBACK_VALUE)
                    }
                }).show()
            }
        }
    }
}
