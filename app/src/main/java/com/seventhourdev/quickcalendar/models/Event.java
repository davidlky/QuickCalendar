package com.seventhourdev.quickcalendar.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by david on 8/5/2016.
 */
public class Event implements Parcelable {
    public String start, end;
    protected Event(Parcel in) {
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
