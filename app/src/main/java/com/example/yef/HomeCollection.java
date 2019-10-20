package com.example.yef;

import java.util.ArrayList;

class HomeCollection {

    String eventname;
    String start_date,end_date,time,event_type,event_details,venue;




    public static ArrayList<HomeCollection> date_collection_arr;

    public HomeCollection(String eventname,String start_date,String end_date, String time, String event_type, String event_details, String venue) {


        this.eventname = eventname;
        this.start_date = start_date;
        this.end_date = end_date;
        this.time = time;
        this.event_type = event_type;
        this.event_details = event_details;
        this.venue = venue;
    }
}
