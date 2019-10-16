package com.example.yef;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    FirebaseUser user;

    FirebaseAuth firebaseAuth;
    FloatingActionButton fab;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));

        Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_downtool);
        toolbar.setAnimation(anim);
        whiteNotificationBar(toolbar);

        fab=(FloatingActionButton) findViewById(R.id.fabadmin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createvent();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();

        user = firebaseAuth.getCurrentUser();

        calendarView=(CalendarView)findViewById(R.id.cview);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(), ""+dayOfMonth+"/"+month+"/"+year, Toast.LENGTH_LONG).show();
            }
        });




    }

    //create event
    void createvent()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create Events!");
        builder.setMessage("Welcome Admin");
        builder.setIcon(R.mipmap.ic_launcher_round);

        final View viewInflated = LayoutInflater.from(this).inflate(R.layout.createvent, (ViewGroup) findViewById(R.id.f1), false);


        EditText eventname=(EditText) viewInflated.findViewById(R.id.eventname);

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

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (user != null) {
            // already signed in
            //Toast.makeText(MainActivity.this, "Welcome admin!", Toast.LENGTH_SHORT).show();
            getMenuInflater().inflate(R.menu.options_menu_logout, menu);
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

                case R.id.maps:
                    Toast.makeText(getApplicationContext(), "Testing Map Intent", Toast.LENGTH_SHORT).show();
                    showdirection();
                    return true;

                case R.id.share:
                    Toast.makeText(getApplicationContext(), "Application share Procedure", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.rate:
                    Toast.makeText(getApplicationContext(), "Rate App", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getApplicationContext(), "Application share Procedure", Toast.LENGTH_LONG).show();
                    return true;
                case R.id.rate:
                    Toast.makeText(getApplicationContext(), "Rate App", Toast.LENGTH_LONG).show();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }



    }

    void showdirection()
    {   String temp="Connought Palace , New Delhi"; //Here , the location from database will be fetched
        Toast.makeText(this, "Please Wait! Showing Possible directions on the map", Toast.LENGTH_LONG).show();
        //https://www.google.com/maps/dir/?api=1&origin=Space+Needle+Seattle+WA&destination=Pike+Place+Market+Seattle+WA&travelmode=bicycling
        //https://www.google.com/maps/dir/?api=1&destination="+l+"&travelmode=walking
        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+temp+"&travelmode=walking");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
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


}
