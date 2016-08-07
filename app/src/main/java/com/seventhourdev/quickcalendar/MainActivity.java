package com.seventhourdev.quickcalendar;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.seventhourdev.quickcalendar.adapters.CalendarListAdapter;
import com.seventhourdev.quickcalendar.models.Event;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpListView();
    }

    private void setUpListView() {
        eventList = new ArrayList<>();

        ListView listView = (ListView) findViewById(R.id.listView);
        assert listView != null;
        CalendarListAdapter listAdapter = new CalendarListAdapter(eventList,this);
        listView.setAdapter(listAdapter);
    }
}
