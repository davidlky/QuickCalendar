package com.seventhourdev.quickcalendar.adapters

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.seventhourdev.quickcalendar.R
import com.seventhourdev.quickcalendar.models.Event
import java.util.*

/**
 * Created by david on 8/5/2016.
 */
class CalendarListAdapter(private val list: ArrayList<Event>, context: Context) : BaseAdapter() {

    private val times = Array(24, {
        if (it < 12) {
            "" + (it + 1) + "AM"
        } else {
            "" + (it - 11) + "PM"
        }
    })
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
        val convertView: View = convertView ?: mInflater.inflate(R.layout.list_item_calendar, null)

        val timeTextView = convertView.findViewById(R.id.time) as TextView
        timeTextView.text = times[position]

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
                            val parentListView = (v.parent.parent as ListView)
                            if (START_POS == position) {
                                (parentListView.getChildAt(position + 1) as LinearLayout).getChildAt(1).setBackgroundColor(Color.WHITE)
                                (parentListView.getChildAt(position - 1) as LinearLayout).getChildAt(1).setBackgroundColor(Color.WHITE)
                            } else if (position - START_POS > 0) {
                                (parentListView.getChildAt(position + 1) as LinearLayout).getChildAt(1).setBackgroundColor(Color.WHITE)
                            } else if (position - START_POS < 0) {
                                (parentListView.getChildAt(position - 1) as LinearLayout).getChildAt(1).setBackgroundColor(Color.WHITE)
                            }
                        }
                    } else {
                        v.setBackgroundColor(Color.BLUE)
                    }
                    ENDING_POS = position
                    if (position > START_POS) {
                        Log.d("asdf", "" + position)
                    } else {
                        Log.d("asdf", "--" + position)
                    }
                }

                DragEvent.ACTION_DRAG_EXITED -> {
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
