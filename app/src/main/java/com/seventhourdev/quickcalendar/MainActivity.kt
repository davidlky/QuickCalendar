package com.seventhourdev.quickcalendar

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.seventhourdev.quickcalendar.adapters.CalendarListAdapter
import com.seventhourdev.quickcalendar.models.Event
import com.seventhourdev.quickcalendar.tools.CalendarAccessor
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmQuery
import java.text.SimpleDateFormat
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
        val config = RealmConfiguration.Builder(this).deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(config)
        setUpDateContainer()
        if(ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUESTED[0]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, PERMISSIONS_REQUESTED[1]) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,PERMISSIONS_REQUESTED,PERMISSION_CALLBACK_VALUE)
        }else{
            setUpListView()
        }
    }

    private fun setUpDateContainer(){
        val format = SimpleDateFormat("dd")
        val calendar = Calendar.getInstance()
        val currDate = calendar.get(Calendar.DAY_OF_MONTH)
        (findViewById(R.id.curr_month) as TextView).text=SimpleDateFormat("MMMM").format(calendar.time)
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        var views = arrayOf(R.id.date1,R.id.date2,R.id.date3,R.id.date4,R.id.date5,R.id.date6,R.id.date7)
        for (i in 0..6) {
            val day = format.format(calendar.time)
            (findViewById(views[i]) as Button).text=day
            if(currDate==day.toInt()){
                val sdk = android.os.Build.VERSION.SDK_INT
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    (findViewById(views[i]) as Button).setBackgroundDrawable(resources.getDrawable(R.drawable.circle_background_white_filled))
                } else {
                    (findViewById(views[i]) as Button).background=resources.getDrawable(R.drawable.circle_background_white_filled)
                }
                (findViewById(views[i]) as Button).setTextColor(resources.getColor(R.color.colorAccent))
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            (findViewById(views[i]) as Button).setOnClickListener {
                Log.d("asdf","asdf")
                val sdk = android.os.Build.VERSION.SDK_INT
                for (i in 0..6) {
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        (findViewById(views[i]) as Button).setBackgroundDrawable(resources.getDrawable(R.drawable.circle_background_white_transparent))
                    } else {
                        (findViewById(views[i]) as Button).background=resources.getDrawable(R.drawable.circle_background_white_transparent)
                    }
                }
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    (findViewById(views[i]) as Button).setBackgroundDrawable(resources.getDrawable(R.drawable.circle_background_white_filled))
                } else {
                    (findViewById(views[i]) as Button).background=resources.getDrawable(R.drawable.circle_background_white_filled)
                }
            }
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
        (findViewById(R.id.calendar_id) as TextView).text=a[0].displayName
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
