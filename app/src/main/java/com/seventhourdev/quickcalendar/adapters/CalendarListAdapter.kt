package com.seventhourdev.quickcalendar.adapters

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.text.Layout
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

import com.seventhourdev.quickcalendar.R
import com.seventhourdev.quickcalendar.models.Event

import java.util.ArrayList

/**
 * Created by david on 8/5/2016.
 */
class CalendarListAdapter(private val list: ArrayList<Event>, private val context: Context) : BaseAdapter() {

    private val times = arrayOf("1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12PM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM", "11PM", "12AM")
    internal var dragged = false
    private val mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)
    }

    override fun getCount(): Int {
        return times.size
    }

    override fun getItem(position: Int): Any {
        return times[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    var START_POS = 0
    var ENDING_POS = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_calendar, null)
        }
        (convertView!!.findViewById(R.id.time) as TextView).text = times[position]

        convertView.findViewById(R.id.event).setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder(view)
                ENDING_POS = position
                START_POS = position
                view.startDrag(data, shadowBuilder, view, 0)
                true
            } else {
                false
            }
        }

        convertView.findViewById(R.id.event).setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    if (v.background != null && (v.background as ColorDrawable).color == Color.BLUE) {
                        if (Math.abs(position - START_POS) < Math.abs(ENDING_POS - START_POS)) {
                            if (START_POS == position) {
                                ((v.parent.parent as ListView).getChildAt(position + 1) as LinearLayout).getChildAt(1).setBackgroundColor(Color.WHITE)
                                ((v.parent.parent as ListView).getChildAt(position - 1) as LinearLayout).getChildAt(1).setBackgroundColor(Color.WHITE)
                            } else {
                                //                                    v.setBackgroundColor(Color.WHITE);
                                if (position - START_POS > 0) {
                                    ((v.parent.parent as ListView).getChildAt(position + 1) as LinearLayout).getChildAt(1).setBackgroundColor(Color.WHITE)
                                } else if (position - START_POS < 0) {
                                    ((v.parent.parent as ListView).getChildAt(position - 1) as LinearLayout).getChildAt(1).setBackgroundColor(Color.WHITE)
                                } else {
                                }
                            }
                        }
                    } else {
                        v.setBackgroundColor(Color.BLUE)
                    }
                    ENDING_POS = position
                    if (position > START_POS) {
                        Log.d("asdf", ""+position)
                    } else {
                        Log.d("asdf", "--"+position)
                    }
                }

                DragEvent.ACTION_DRAG_EXITED -> {
                    if (Math.abs(position - START_POS) < Math.abs(ENDING_POS - START_POS)) {
                        if (START_POS == position) {

                        } else {
                            v.setBackgroundColor(Color.WHITE)
                        }
                    }
                    //                        ENDING_POS = position;
                    Log.d("asdf", "exit--" + position)
                }
                DragEvent.ACTION_DROP -> {
                    Log.d("asdf", "drop--")
                    ENDING_POS = position
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    //update list view if drop valid (inside list view)
                    if (event.result && position == START_POS) {
                        Log.d("listView", "result = TRUE")
                        Log.d("asdf", "drag ended--" + START_POS + "to" + ENDING_POS)
                        START_POS = -1
                    }
                    v.setBackgroundColor(Color.WHITE)
                }
                else -> {
                }
            }
            true
        }
        return convertView
    }
}
