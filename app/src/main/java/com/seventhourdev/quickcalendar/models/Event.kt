package com.seventhourdev.quickcalendar.models

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by david on 8/5/2016.
 */
class Event protected constructor(`in`: Parcel) : Parcelable {
    var start: String? = null
    var end: String? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
    }

    companion object {

        val CREATOR: Parcelable.Creator<Event> = object : Parcelable.Creator<Event> {
            override fun createFromParcel(`in`: Parcel): Event {
                return Event(`in`)
            }

            override fun newArray(size: Int): Array<Event> {
                return arrayOfNulls(size)
            }
        }
    }
}
