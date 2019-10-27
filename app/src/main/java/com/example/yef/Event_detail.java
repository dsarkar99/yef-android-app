package com.example.yef;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Objects;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Event_detail extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detail);

        String s1=Objects.requireNonNull(getIntent().getExtras()).getString("eventname");
        final String s2=Objects.requireNonNull(getIntent().getExtras()).getString("venue");
        String s3=Objects.requireNonNull(getIntent().getExtras()).getString("esdate");
        String s4=Objects.requireNonNull(getIntent().getExtras()).getString("eedate");
        String s5=Objects.requireNonNull(getIntent().getExtras()).getString("etime");
        String s6=Objects.requireNonNull(getIntent().getExtras()).getString("etype");
        String s7=Objects.requireNonNull(getIntent().getExtras()).getString("edetails");



        TextView ename=(TextView)findViewById(R.id.event_name_display);
        TextView eplace=(TextView)findViewById(R.id.eventplace_display);
        TextView esdate=(TextView)findViewById(R.id.eventSdate_display);
        TextView eedate=(TextView)findViewById(R.id.eventEdate_display);
        TextView etime=(TextView)findViewById(R.id.eventTime_display);
        TextView etype=(TextView)findViewById(R.id.eventType_display);
        TextView edetails=(TextView)findViewById(R.id.eventHost_display);

        Button btn_direction=(Button)findViewById(R.id.btn_direction);
        btn_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_direction(s2);
            }
        });

        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.faback);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ename.setText(s1);
        eplace.setText(s2);
        esdate.setText(s3);
        eedate.setText(s4);
        etime.setText(s5);
        etype.setText(s6);
        edetails.setText(s7);

/*        firebaseAuth = FirebaseAuth.getInstance();
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // already signed in
                    Toast.makeText(Event_detail.this, "Welcome admin!", Toast.LENGTH_SHORT).show();
                } else {
                    // not signed in
                    startActivityForResult(
                            // Get an instance of AuthUI based on the default app
                            AuthUI.getInstance().createSignInIntentBuilder()
                                    .setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .build(), 1);


                }
            }
        };*/


    }

/*    //method to check successful sign in
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //do nothing
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(listener);

    }*/

    void open_direction(String l)
    {
        Toast.makeText(this, "Please Wait! Showing Possible directions on the map", Toast.LENGTH_LONG).show();
        try {
            String[] str_array = l.split(",");
            String latitute = str_array[0];
            String longitude = str_array[1];
        } catch (NullPointerException e) {
            System.out.println(e.toString());
        }

        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+l+"&travelmode=driving");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Event_detail.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
