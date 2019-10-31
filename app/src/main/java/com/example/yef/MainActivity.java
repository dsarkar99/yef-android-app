package com.example.yef;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
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
    Bitmap bitmap;
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

    ProgressBar progressBar,progressBarcircle;

    Toolbar toolbar;

    String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    String selectedGridDate;

    boolean doubleBackToExitPressedOnce = false;

    LinearLayout calendarcontainer;

    public static final String MyPREFERENCES = "MyPrefs";

    boolean hasLoggedIn;
    String usertype,name;

    SharedPreferences sharedPreferences;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //whiteNotificationBar(findViewById(R.id.constraintlayoutroot));

        if (Build.VERSION.SDK_INT >= 17) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_downtool);
        toolbar.setAnimation(anim);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        SharedPreferences settings = getSharedPreferences(LoginActivity.MyPREFERENCES, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        hasLoggedIn = settings.getBoolean("hasloggedin", false);
        usertype = settings.getString("user_type", "yes");
        name=settings.getString("name", "");

        if(name.equals(""))
        {
            findViewById(R.id.cardviewtxt).setVisibility(View.GONE);
        }



        //whiteNotificationBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.my_progressBar);
        //progressBarcircle=(ProgressBar)findViewById(R.id.progress_circular);

        calendarcontainer=(LinearLayout)findViewById(R.id.calendarContainer);

        HomeCollection.date_collection_arr= new ArrayList<>();



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
        //whiteNotificationBar(recyclerView);

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
/*                if (cal_month.get(GregorianCalendar.MONTH) == 4&&cal_month.get(GregorianCalendar.YEAR)==2017) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(MainActivity.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {*/
                    setPreviousMonth();
                    refreshCalendar();
               // }


            }
        });
        ImageButton next = (ImageButton) findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                if (cal_month.get(GregorianCalendar.MONTH) == 5&&cal_month.get(GregorianCalendar.YEAR)==2018) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(MainActivity.this, "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {*/
                    setNextMonth();
                    refreshCalendar();
               // }
            }
        });
        GridView gridview = (GridView) findViewById(R.id.gv_calendar);
        gridview.setAdapter(hwAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                selectedGridDate = HwAdapter.day_string.get(position);
                get_edetails(selectedGridDate);
                //Toast.makeText(MainActivity.this, selectedGridDate, Toast.LENGTH_SHORT).show();
               // ((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, MainActivity.this);
            }

        });

        movingtextintoolbar();
        get_events();


    }

    void movingtextintoolbar()
    {
        TextView titleTextView = null;

        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);

            titleTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            titleTextView.setFocusable(true);
            titleTextView.setFocusableInTouchMode(true);
            titleTextView.requestFocus();
            titleTextView.setSingleLine(true);
            titleTextView.setSelected(true);
            titleTextView.setMarqueeRepeatLimit(-1);

        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
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

        builder.setPositiveButton("Submit", null);
// Set up the buttons
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //builder.show();


        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    b.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            //Validation check
                            if(eventplace.getText().toString().trim().isEmpty()||eventname.getText().toString().trim().isEmpty()||esdate.getText().toString().trim().isEmpty()||eedate.getText().toString().trim().isEmpty()||eventtype.getText().toString().trim().isEmpty()||eventTime.getText().toString().trim().isEmpty()||eventdetails.getText().toString().trim().isEmpty())
                            {
                                Toast.makeText(MainActivity.this, "These are mandatory fields, Dont keep them empty",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                create_event(eventname.getText().toString().trim(),eventplace.getText().toString().trim(),esdate.getText().toString().trim(),eedate.getText().toString().trim(),eventtype.getText().toString().trim(),eventTime.getText().toString().trim(),eventdetails.getText().toString().trim());
                                dialog.dismiss();
                            }
                        }
                    });
            }
        });
        mAlertDialog.show();



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
        if (hasLoggedIn) {
            // already signed in
            //Toast.makeText(MainActivity.this, "Welcome admin!", Toast.LENGTH_SHORT).show();
            if(usertype.equals("Admin"))
            {
                getMenuInflater().inflate(R.menu.options_menu_admin, menu);
                fab.setVisibility(View.VISIBLE);
            }
            else
            {
                getMenuInflater().inflate(R.menu.options_menu_admin, menu);
                fab.setVisibility(View.GONE);
            }

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
        if (hasLoggedIn) {
            switch (id){
                case R.id.profile:
                    startActivity(new Intent(MainActivity.this,Account.class));
                    finish();
                    return true;
                case R.id.share:
                    appShare();
                    return true;
                case R.id.rate:
                    appRate();
                    return true;
                case R.id.logout:
                    logout();
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
                    //login();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
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

    void logout()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("hasloggedin",false);
        editor.putString("name", "");
        editor.putString("user_type", "");
        editor.putString("userid", "");
        editor.putString("email", "");
        editor.apply();
        Toast.makeText(getApplicationContext(), "Successfully logged out!", Toast.LENGTH_LONG).show();
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
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
        esdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    void showDate2(int year, int month, int day) {
        eedate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    void get_events()
    {   progressBar.setVisibility(View.VISIBLE);
        TextView textView=(TextView)findViewById(R.id.textname);
        textView.setText("Hi, "+name+"!");
        @SuppressLint("ResourceAsColor") JsonArrayRequest request = new JsonArrayRequest(getString(R.string.url)+"get_events.php",
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
                    progressBar.setVisibility(View.GONE);
                    calendarcontainer.setVisibility(View.VISIBLE);
                    refreshCalendar();

                }, error -> {
                progressBar.setVisibility(View.GONE);
                findViewById(R.id.internet).setVisibility(View.VISIBLE);
                findViewById(R.id.constraintlayoutroot).setBackgroundColor(R.color.white);
                Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "Looks there's some network issue !", Snackbar.LENGTH_INDEFINITE);
                sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                sb.setAction("RETRY?", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(getIntent());
                    finish();
                }
            });
            sb.setActionTextColor(getResources().getColor(R.color.red));
            sb.show();
            //Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }



    void create_event(final String ename, final String eplace, final String sdate, final String edate, final String etype, final String etime, final String edetails)
    {
        @SuppressLint("StaticFieldLeak")
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            private ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(MainActivity.this, "Creating a Event...", "Please Wait...");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                if(Objects.equals(s.trim(), "error"))
                {
                    Toast.makeText(MainActivity.this, "Server error,Try again later!",Toast.LENGTH_LONG).show();
                    loading.dismiss();
                }
                else
                {   loading.dismiss();
                    Toast.makeText(MainActivity.this, "Successfully scheduled "+ename+" on "+sdate+" at "+etime,Toast.LENGTH_LONG).show();

                    Intent intent=getIntent();
                    startActivity(intent);
                    finish();
                    //refreshCalendar();
                }

                //Toast.makeText(SellingPortal.this, s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap = params[0];

                HashMap<String,String> data = new HashMap<>();
                data.put("ename",ename);
                data.put("eplace",eplace);
                data.put("sdate",sdate);
                data.put("edate",edate);
                data.put("etype",etype);
                data.put("etime",etime);
                data.put("edetails",edetails);
                String result = rh.sendPostRequest(getString(R.string.url)+"create_events.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
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
                progressBar.setVisibility(View.VISIBLE);


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
                        //Toast.makeText(MainActivity.this,"Request Timed out!\n" + "Refresh Now..",Toast.LENGTH_LONG).show();
                        Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "Request Timed out!" + "Refresh Now..", Snackbar.LENGTH_LONG);
                        sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                        sb.show();
                        contactList.clear();
                        mAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                    else if(Objects.equals(s.trim(), "")) {
                        Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "Unable to fetch data!", Snackbar.LENGTH_LONG);
                        sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                        sb.show();
                        //Toast.makeText(MainActivity.this, "Unable to fetch data!", Toast.LENGTH_LONG).show();
                        contactList.clear();
                        mAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }
                    else if(Objects.equals(s.trim(), "[]")) {
                        contactList.clear();
                        mAdapter.notifyDataSetChanged();
                        //Toast.makeText(MainActivity.this, "Sorry, No events on "+selectedGridDate+"!", Toast.LENGTH_LONG).show();
                        Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "Sorry, No events on "+selectedGridDate+"!", Snackbar.LENGTH_LONG);
                        sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                        sb.show();
                        //Snackbar.make(findViewById(R.id.constraintlayoutroot), "Sorry, No events on "+selectedGridDate+"!", Snackbar.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                    else
                    {
                        List<modelclass> items = new Gson().fromJson(s, new TypeToken<List<modelclass>>() {
                        }.getType());


                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);

                        final LayoutAnimationController controller =
                                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_up_to_down);

                        recyclerView.setLayoutAnimation(controller);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();

                        progressBar.setVisibility(View.GONE);
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(MainActivity.this, e.toString(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
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

    private void transparentToolbar() {
        if (Build.VERSION.SDK_INT >= 17 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_FULLSCREEN, true);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }


    private void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onBackPressed() {

            if (doubleBackToExitPressedOnce) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }

            this.doubleBackToExitPressedOnce = true;

            Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "Press BACK again to exit!", Snackbar.LENGTH_LONG);
            sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
/*            sb.setAction("Exit?", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //locationManager.removeUpdates(MainActivity.this);
                    *//*android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);*//*
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
            sb.setActionTextColor(getResources().getColor(R.color.red));*/
            sb.show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

    }
}