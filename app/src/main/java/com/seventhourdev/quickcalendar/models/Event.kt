package com.seventhourdev.quickcalendar.models

import io.realm.RealmObject
import android.os.Parcel
import android.os.Parcelable
import io.realm.annotations.Ignore
import io.realm.annotations.RealmClass
import java.security.Timestamp
import java.util.*

/**
 * Created by david on 8/5/2016.
 */
open class Event(open var startLong: Long = GregorianCalendar().timeInMillis,
                 open var endLong: Long=GregorianCalendar().timeInMillis,
                 @Ignore open var start: GregorianCalendar = GregorianCalendar(),
                 @Ignore open var end: GregorianCalendar = GregorianCalendar()
                 ) : RealmObject(){
    public fun upDateTimes(newStart:GregorianCalendar, newEnd:GregorianCalendar){
        start=newStart
        end=newEnd
        startLong=newStart.timeInMillis
        endLong=newEnd.timeInMillis
    }
}
