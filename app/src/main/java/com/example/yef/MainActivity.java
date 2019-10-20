package com.example.yef;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.yef.Model.modelclass;
import com.example.yef.adapter.eventadapter;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;



public class MainActivity extends AppCompatActivity {

    FirebaseUser user;

    FirebaseAuth firebaseAuth;
    FloatingActionButton fab;

    private Context context = this;

    String date;

    FirebaseDatabase  database;
    DatabaseReference ref;

    DatePicker datePicker;
    Calendar calendar;
    TextView dateshow;
    int year, month, day;

    private RecyclerView recyclerView;
    private List<modelclass> contactList;
    private eventadapter mAdapter;

    TextView esdate,eedate,eventTime;
    EditText eventname,eventplace,eventtype,eventdetails;

    public GregorianCalendar cal_month, cal_month_copy;
    private HwAdapter hwAdapter;
    private TextView tv_month;

    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_downtool);
        toolbar.setAnimation(anim);
        whiteNotificationBar(toolbar);

        HomeCollection.date_collection_arr= new ArrayList<>();

        get_events();


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        fab=(FloatingActionButton) findViewById(R.id.fabadmin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this,temporary.class));
                createvent();
            }
        });



        recyclerView = findViewById(R.id.recyclerView);
        contactList = new ArrayList<>();
        mAdapter = new eventadapter(MainActivity.this, contactList);

        // white background notification bar
        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);





        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        Date c = Calendar.getInstance().getTime();

        //final String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

/*        CalendarView cv=(CalendarView)findViewById(R.id.cview);
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int monthtemp=month+1;
                date=dayOfMonth+"-"+monthtemp+"-"+year;
                //Toast.makeText(getApplicationContext(), date, Toast.LENGTH_LONG).show();
                get_data();
                //Toast.makeText(getApplicationContext(), ""+dayOfMonth+"-"+month+"-"+year, Toast.LENGTH_LONG).show();
            }
        });*/





        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        hwAdapter = new HwAdapter(this, cal_month,HomeCollection.date_collection_arr);

        tv_month = (TextView) findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));


        ImageButton previous = (ImageButton) findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 4&&cal_month.get(GregorianCalendar.YEAR)==2017) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(MainActivity.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setPreviousMonth();
                    refreshCalendar();
                }


            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 5&&cal_month.get(GregorianCalendar.YEAR)==2018) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(MainActivity.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });
        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(hwAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = HwAdapter.day_string.get(position);
                get_edetails(selectedGridDate);
                Toast.makeText(MainActivity.this, selectedGridDate, Toast.LENGTH_SHORT).show();
               // ((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, MainActivity.this);
            }

        });
    }

    //create event
    void createvent()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
/*        builder.setTitle("Create Events!");
        builder.setMessage("Welcome Admin");
        builder.setIcon(R.mipmap.ic_launcher_round);*/

        final View viewInflated = LayoutInflater.from(this).inflate(R.layout.createvent, (ViewGroup) findViewById(R.id.f1), false);


        eventname = viewInflated.findViewById(R.id.eventname);
        eventplace=viewInflated.findViewById(R.id.eventplace);

        esdate=viewInflated.findViewById(R.id.esdate);
        eedate=viewInflated.findViewById(R.id.eedate);
        eventTime=viewInflated.findViewById(R.id.eventTime);
        eventtype=viewInflated.findViewById(R.id.eventType);
        eventdetails=viewInflated.findViewById(R.id.eventdetails);


        eventTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        eventTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });



        esdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 setsDate(viewInflated);
            }
        });

        eedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seteDate(viewInflated);
            }
        });

        //showDate(year, month+1, day);

        builder.setView(viewInflated);

        builder.setCancelable(false);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String Date=dateshow.getText().toString();
                String uuid= UUID.randomUUID().toString();
                String Eventname=eventname.getText().toString();
                HashMap<String,String> eventMap=new HashMap<>();
                eventMap.put("Event Name: ",Eventname);
                ref.child("Events").child("Date: "+Date).child("Event Id: "+ uuid).setValue(eventMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {

                                    Toast.makeText(MainActivity.this,"Event Created Successfully",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    String message=task.getException().toString();
                                    Toast.makeText(MainActivity.this,"Error: "+message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
// Set up the buttons
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();









    }

    //    Function to get to home page activity on title text click.
    public void getToHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

/*    void choosedate()
    {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setYearRange(1985, 2028);
        datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);

    }*/

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (user != null) {
            // already signed in
            //Toast.makeText(MainActivity.this, "Welcome admin!", Toast.LENGTH_SHORT).show();
            getMenuInflater().inflate(R.menu.options_menu_admin, menu);
            fab.setVisibility(View.VISIBLE);
            return true;
        }
        else
        {
            getMenuInflater().inflate(R.menu.options_menu, menu);
            fab.setVisibility(View.GONE);
            return true;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (user != null) {
            switch (id){
                case R.id.dashboard:
                    //intent for opening event details activity
                    createvent();
                    return true;
                case R.id.share:
                    appShare();
                    return true;
                case R.id.rate:
                    appRate();
                    return true;
                case R.id.logout:
                    Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    Intent i=getIntent();
                    startActivity(i);
                    finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        else
        {
            switch (id){
                case R.id.admin_link:
                    //intent for opening event details activity
                    //Admin login

                    login();
                    //Intent intent = new Intent(MainActivity.this, Event_detail.class);
                    //startActivity(intent);
                    //finish();
                    return true;
                case R.id.share:
                    appShare();
                    return true;
                case R.id.rate:
                    appRate();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    void login()
    {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // already signed in
                    Toast.makeText(MainActivity.this, "Already Signed In!", Toast.LENGTH_SHORT).show();
                } else {
                    // not signed in
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app
                            AuthUI.getInstance().createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(), 1);

                }
    }

    //method to check successful sign in
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //do nothing
                Intent i=getIntent();
                startActivity(i);
                finish();
                Toast.makeText(MainActivity.this, "Welcome Admin!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                //finish();
            }
        }
    }

    private void appShare(){
//        Method to share app with friends
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
            String shareMessage= "\nLet me recommend you this application\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch(Exception e) {
            //e.toString();
        }
    }

    private void appRate(){
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }


    @SuppressWarnings("deprecation")
    public void setsDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Please Choose a Event start date",
                Toast.LENGTH_SHORT)
                .show();
    }

    public void seteDate(View view) {
        showDialog(1000);
        Toast.makeText(getApplicationContext(), "Please Choose a Event end date",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }

        if (id == 1000) {
            return new DatePickerDialog(this,
                    myDateListener2, year, month, day);
        }
        return null;
    }

    DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
/*                    arg1 = year;
                    arg2 = month;
                    arg3 = day;*/
                    showDate2(arg1, arg2+1, arg3);
                }
            };

    DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
/*                    arg1 = year;
                    arg2 = month;
                    arg3 = day;*/
                    showDate(arg1, arg2+1, arg3);
                }
            };

    void showDate(int year, int month, int day) {
        esdate.setText(new StringBuilder().append(day).append("-")
                .append(monthNames[month-1]).append("-").append(year));
    }

    void showDate2(int year, int month, int day) {
        eedate.setText(new StringBuilder().append(day).append("-")
                .append(monthNames[month-1]).append("-").append(year));
    }

    void get_events()
    {
        JsonArrayRequest request = new JsonArrayRequest(getString(R.string.url)+"get_events.php",
                response -> {
                    //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                    if (response == null) {
                        Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    for (int i = 0; i < response.length(); i++) {

                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            //imageView.setImageResource(sampleImages[position]);
                            //Toast.makeText(MainActivity.this, jsonObject.getString("image_url"), Toast.LENGTH_LONG).show();
                            //date_collection_arr.add(new HomeCollection("ABC EVENT","2019-09-20","2019-09-20","15:33","XYZ","OUTSTANDING","Kolkata"));
                            HomeCollection.date_collection_arr.add( new HomeCollection(jsonObject.getString("eventname"),jsonObject.getString("start_date"),jsonObject.getString("end_date"),jsonObject.getString("time"),jsonObject.getString("event_type"),jsonObject.getString("event_details"),jsonObject.getString("venue")));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //PD.dismiss();
                    }
                }, error -> {
            //Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }


    void get_edetails(String dateresponse)
    {
        final Bitmap[] bitmap = new Bitmap[1];
        @SuppressLint("StaticFieldLeak")
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            HttpHandler rh = new HttpHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

               // Toast.makeText(MainActivity.this, s,Toast.LENGTH_LONG).show();
                try
                {
                    if(Objects.equals(s.trim(), "error"))
                    {
                        Toast.makeText(MainActivity.this,"Request Timed out!\n" + "Refresh Now..",Toast.LENGTH_LONG).show();
                        contactList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                    else if(Objects.equals(s.trim(), "")) {
                        Toast.makeText(MainActivity.this, "Unable to fetch data!", Toast.LENGTH_LONG).show();
                        contactList.clear();
                        mAdapter.notifyDataSetChanged();
                    }
                    else if(Objects.equals(s.trim(), "[]")) {
                        contactList.clear();
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Sorry, No events on "+date+"!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        List<modelclass> items = new Gson().fromJson(s, new TypeToken<List<modelclass>>() {
                        }.getType());


                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, e.toString(),Toast.LENGTH_LONG).show();
                }

                //Toast.makeText(SellingPortal.this, s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap[0] = params[0];

                HashMap<String,String> data = new HashMap<>();
                data.put("eventdate",dateresponse.trim());
                String result = rh.sendPostRequest(getString(R.string.url)+"get_edetails.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap[0]);
    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        hwAdapter.refreshDays();
        hwAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }
}