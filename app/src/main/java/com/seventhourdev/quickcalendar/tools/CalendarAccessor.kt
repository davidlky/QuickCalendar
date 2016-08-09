package com.seventhourdev.quickcalendar.tools

import android.content.Context
import android.database.Cursor
import android.provider.CalendarContract
import com.seventhourdev.quickcalendar.models.Calendar
import com.seventhourdev.quickcalendar.models.Event
import java.util.*

object CalendarAccessor{
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    val CALENDAR_PROJECTION = arrayOf(CalendarContract.Calendars._ID, // 0
            CalendarContract.Calendars.ACCOUNT_NAME, // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    )
    val  EVENT_PROJECTION = arrayOf(
            CalendarContract.Events.DTSTART,
            CalendarContract.Events.DTEND,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.ORGANIZER,
            CalendarContract.Events.EVENT_LOCATION,
            CalendarContract.Events.RRULE,
            CalendarContract.Events.RDATE,
            CalendarContract.Events.ALL_DAY,
            CalendarContract.Events.VISIBLE,
            CalendarContract.Events.CALENDAR_ID,
            CalendarContract.Events.DURATION,
            CalendarContract.Events.EVENT_COLOR_KEY,
            CalendarContract.Events.EVENT_TIMEZONE
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
        cur = cr.query(uri, CALENDAR_PROJECTION, selection, selectionArgs, null)
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

    fun getAllEvents(id:Long):Array<Event>{
        val selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?)"
        val selectionArgs = arrayOf(""+id)
        return eventQueryAndReturn(selection,selectionArgs)
    }

    fun getAllEventsInRange(id:Long, start:Long, end:Long):Array<Event>{
        val selection = "(" + CalendarContract.Events.CALENDAR_ID + " = ?) AND ( "+ CalendarContract.Events.DTSTART + " >= ? ) AND (" + CalendarContract.Events.DTEND + "<= ?)"
        val selectionArgs = arrayOf(""+id, ""+start, ""+end)
        return eventQueryAndReturn(selection,selectionArgs)
    }

    private fun eventQueryAndReturn(query:String, queryArgs: Array<String>):Array<Event>{
        // Run query
        var calendars = ArrayList<Event>()
        var cur: Cursor? = null
        val uri = CalendarContract.Calendars.CONTENT_URI
        val cr = context!!.getContentResolver()
        cur = cr.query(uri, EVENT_PROJECTION, query, queryArgs, null)
        // Use the cursor to step through the returned records
        while (cur.moveToNext()) {
            // Get the field values
            var event = Event(
                    cur.getLong(cur.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)),
                    cur.getLong(cur.getColumnIndexOrThrow(CalendarContract.Events.DTEND)),
                    cur.getLong(cur.getColumnIndexOrThrow(CalendarContract.Events.CALENDAR_ID)),
                    cur.getString(cur.getColumnIndexOrThrow(CalendarContract.Events.ORGANIZER)),
                    cur.getString(cur.getColumnIndexOrThrow(CalendarContract.Events.TITLE)),
                    cur.getString(cur.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION)),
                    cur.getString(cur.getColumnIndexOrThrow(CalendarContract.Events.EVENT_LOCATION)),
                    cur.getString(cur.getColumnIndexOrThrow(CalendarContract.Events.EVENT_TIMEZONE)))
            event.start.timeInMillis=cur.getLong(cur.getColumnIndexOrThrow(CalendarContract.Events.DTSTART))
            event.end.timeInMillis=cur.getLong(cur.getColumnIndexOrThrow(CalendarContract.Events.DTEND))
            calendars.add(event)
        }
        return calendars.toTypedArray()

    }
}
