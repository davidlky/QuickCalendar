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
import io.realm.Realm
import java.text.SimpleDateFormat
import java.util.*

class CalendarListAdapter(private var list: ArrayList<Event>, val context: Context) : BaseAdapter() {

    private val times = Array(24, {
        if (it < 12) {
            "" + (it + 1) + "AM"
        } else {
            "" + (it - 11) + "PM"
        }
    })

    var START_POS = 0
    var ENDING_POS = 0

    var highlighted = Array<Boolean>(24,{it->false})

    private val mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)
        getHighlightedHours()
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

    fun getHighlightedHours(){
        highlighted = Array<Boolean>(24,{it->false})
        for (event in list){
            for (i in event.start.get(GregorianCalendar.HOUR_OF_DAY)-1..event.end.get(GregorianCalendar.HOUR_OF_DAY)-1){
                highlighted[i]=true
            }
        }
    }

    override fun notifyDataSetChanged() {
        super.notifyDataSetChanged()
        getHighlightedHours()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val convertView: View = convertView ?: mInflater.inflate(R.layout.list_item_calendar,null)

        val timeTextView = convertView.findViewById(R.id.time) as TextView
        timeTextView.text = times[position]

        convertView.findViewById(R.id.event).setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = View.DragShadowBuilder()
                ENDING_POS = position
                START_POS = position
                view.startDrag(data, shadowBuilder, view, 0)
                true
            } else {
                false
            }
        }

        val colorAccent: Int = context.resources.getColor(R.color.colorAccent,context.theme)
        val colorReset: Int = Color.TRANSPARENT

        if (highlighted[position]){
            (convertView as LinearLayout).getChildAt(1).setBackgroundColor(colorAccent)
        }else{
            (convertView as LinearLayout).getChildAt(1).setBackgroundColor(colorReset)
        }

        convertView.findViewById(R.id.event).setOnDragListener { v, event ->
            val parentListView = (v.parent.parent as ListView)
            when (event.action) {
                DragEvent.ACTION_DRAG_ENTERED -> {
                    if (v.background != null && (v.background as ColorDrawable).color == colorAccent) {
                        if (Math.abs(position - START_POS) < Math.abs(ENDING_POS - START_POS)) {
                            if (START_POS == position) {
                                (parentListView.getChildAt(position + 1) as LinearLayout).getChildAt(1).setBackgroundColor(colorReset)
                                (parentListView.getChildAt(position - 1) as LinearLayout).getChildAt(1).setBackgroundColor(colorReset)
                            } else if (position - START_POS > 0) {
                                (parentListView.getChildAt(position + 1) as LinearLayout).getChildAt(1).setBackgroundColor(colorReset)
                            } else if (position - START_POS < 0) {
                                (parentListView.getChildAt(position - 1) as LinearLayout).getChildAt(1).setBackgroundColor(colorReset)
                            }
                        }
                    } else {
                        v.setBackgroundColor(colorAccent)
                    }
                    ENDING_POS = position
                    val timeText = if (position<START_POS){
                        "" + times[position] + " - " + times[START_POS]
                    }else{
                        "" + times[START_POS] + " - " + times[position]
                    }
                    ((parentListView.getChildAt(START_POS-parentListView.firstVisiblePosition) as LinearLayout).getChildAt(1) as TextView).text=timeText
                    Log.d("asdf", timeText)
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
                        ((parentListView.getChildAt(START_POS-parentListView.firstVisiblePosition) as LinearLayout).getChildAt(1) as TextView).text=""
                        val startTime = GregorianCalendar()
                        val endTime = GregorianCalendar()
                        if (position<START_POS){
                            startTime.set(GregorianCalendar.HOUR_OF_DAY,ENDING_POS+1)
                            endTime.set(GregorianCalendar.HOUR_OF_DAY,START_POS+1)
                        }else{
                            startTime.set(GregorianCalendar.HOUR_OF_DAY,START_POS+1)
                            endTime.set(GregorianCalendar.HOUR_OF_DAY,ENDING_POS+1)
                        }
                        with(startTime){
                            set(GregorianCalendar.MINUTE,0)
                            set(GregorianCalendar.SECOND,0)
                            set(GregorianCalendar.MILLISECOND,0)
                        }
                        with(endTime){
                            set(GregorianCalendar.MINUTE,0)
                            set(GregorianCalendar.SECOND,0)
                            set(GregorianCalendar.MILLISECOND,0)
                        }
                        val realm = Realm.getDefaultInstance()
                        realm.executeTransaction {
                            var event = realm.createObject(Event::class.java)
                            event.upDateTimes(startTime,endTime)
                            list.add(event)
                        }
                        notifyDataSetChanged()
                        START_POS = -1
                    }
                    v.setBackgroundColor(colorReset)
                }
                else -> {
                }
            }
            true
        }
        return convertView
    }
}
