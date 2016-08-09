package com.seventhourdev.quickcalendar.models

import io.realm.RealmObject
import android.os.Parcel
import android.os.Parcelable
import io.realm.annotations.Ignore
import io.realm.annotations.RealmClass
import java.security.Timestamp
import java.util.*
import java.util.Calendar

/**
 * Created by david on 8/5/2016.
 */
open class Event(open var startLong: Long = GregorianCalendar().timeInMillis,
                 open var endLong: Long=GregorianCalendar().timeInMillis,
                 open var calendarId: Long=GregorianCalendar().timeInMillis,
                 open var organizer: String="",
                 open var title: String="",
                 open var description: String="",
                 open var event_location: String="",
                 open var timezone: String= Calendar.getInstance().timeZone.displayName,
                 @Ignore open var start: GregorianCalendar = GregorianCalendar(),
                 @Ignore open var end: GregorianCalendar = GregorianCalendar()
                 ) : RealmObject(){
    fun upDateTimes(newStart:GregorianCalendar, newEnd:GregorianCalendar){
        start=newStart
        end=newEnd
        startLong=newStart.timeInMillis
        endLong=newEnd.timeInMillis
    }
}
