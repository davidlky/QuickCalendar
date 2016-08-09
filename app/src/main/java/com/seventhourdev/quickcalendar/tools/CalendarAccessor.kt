package com.seventhourdev.quickcalendar.tools

import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract
import com.seventhourdev.quickcalendar.models.Calendar
import java.util.*

object CalendarAccessor{
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    val EVENT_PROJECTION = arrayOf(CalendarContract.Calendars._ID, // 0
            CalendarContract.Calendars.ACCOUNT_NAME, // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    )

    var context:Context?=null


    // The indices for the projection array above.
    private val PROJECTION_ID_INDEX = 0
    private val PROJECTION_ACCOUNT_NAME_INDEX = 1
    private val PROJECTION_DISPLAY_NAME_INDEX = 2
    private val PROJECTION_OWNER_ACCOUNT_INDEX = 3

    fun getAllCalendars():Array<Calendar>{
        // Run query
        var cur: Cursor? = null
        var calendars = ArrayList<Calendar>()
        val cr = context!!.getContentResolver()
        val uri = CalendarContract.Calendars.CONTENT_URI
        val selection = "(" + CalendarContract.Calendars.VISIBLE + " = ?)"
        val selectionArgs = arrayOf("1")
        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null)
        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
            // Get the field values
            var calendar = Calendar(
                    cur.getLong(PROJECTION_ID_INDEX),
                    cur.getString(PROJECTION_DISPLAY_NAME_INDEX),
                    cur.getString(PROJECTION_ACCOUNT_NAME_INDEX),
                    cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX))
            calendars.add(calendar)
        }
        return calendars.toTypedArray()
    }
}
