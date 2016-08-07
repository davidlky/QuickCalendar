package com.seventhourdev.quickcalendar.adapters;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.text.Layout;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.seventhourdev.quickcalendar.R;
import com.seventhourdev.quickcalendar.models.Event;

import java.util.ArrayList;

/**
 * Created by david on 8/5/2016.
 */
public class CalendarListAdapter extends BaseAdapter {
    private ArrayList<Event> list;

    private String[] times = new String[]{
            "1AM","2AM","3AM","4AM","5AM","6AM","7AM","8AM","9AM","10AM","11AM","12PM","1PM","2PM","3PM","4PM","5PM","6PM","7PM","8PM","9PM","10PM","11PM","12AM"
    };
    boolean dragged = false;
    private LayoutInflater mInflater;
    private Context context;
    public CalendarListAdapter(ArrayList<Event> list,Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return times.length;
    }

    @Override
    public Object getItem(int position) {
        return times[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public int START_POS = 0;
    public int ENDING_POS = 0;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_calendar,null);
        }
        ((TextView)convertView.findViewById(R.id.time)).setText(times[position]);

        convertView.findViewById(R.id.event).setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    ENDING_POS = position;
                    START_POS = position;
                    view.startDrag(data, shadowBuilder, view, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });

        convertView.findViewById(R.id.event).setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(final View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        if(v.getBackground() !=null&&((ColorDrawable)v.getBackground()).getColor()==Color.BLUE) {
                            if (Math.abs(position - START_POS) < Math.abs(ENDING_POS - START_POS)) {
                                if (START_POS == position) {
                                    ((LinearLayout) ((ListView) v.getParent().getParent()).getChildAt(position + 1)).getChildAt(1).setBackgroundColor(Color.WHITE);
                                    ((LinearLayout) ((ListView) v.getParent().getParent()).getChildAt(position - 1)).getChildAt(1).setBackgroundColor(Color.WHITE);
                                } else {
//                                    v.setBackgroundColor(Color.WHITE);
                                    if(position - START_POS>0) {
                                        ((LinearLayout) ((ListView) v.getParent().getParent()).getChildAt(position + 1)).getChildAt(1).setBackgroundColor(Color.WHITE);
                                    }else if(position-START_POS<0){
                                        ((LinearLayout) ((ListView) v.getParent().getParent()).getChildAt(position - 1)).getChildAt(1).setBackgroundColor(Color.WHITE);
                                    }else{
                                    }
                                }
                            }
                        }else{
                            v.setBackgroundColor(Color.BLUE);
                        }
                        ENDING_POS = position;
                        if(position > START_POS){
                            Log.d("asdf",position+"");
                        }
                        else{
                            Log.d("asdf",position+"--");
                        }
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        if (Math.abs(position-START_POS)<Math.abs(ENDING_POS-START_POS)){
                            if(START_POS==position){

                            }else {
                                v.setBackgroundColor(Color.WHITE);
                            }
                        }
//                        ENDING_POS = position;
                        Log.d("asdf","exit--"+position);
                        break;
                    case DragEvent.ACTION_DROP:
                        Log.d("asdf","drop--");
                        ENDING_POS = position;
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        //update list view if drop valid (inside list view)
                        if (event.getResult()&&position==START_POS){
                            Log.d("listView", "result = TRUE");
                            Log.d("asdf","drag ended--"+START_POS+"to"+ENDING_POS);
                            START_POS = -1;
                        }
                        v.setBackgroundColor(Color.WHITE);

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        return convertView;
    }
}
