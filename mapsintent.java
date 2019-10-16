package com.example.yef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Event_details extends AppCompatActivity {
    TextView text;
    Button btn;
    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        text=findViewById(R.id.textloc);
        btn=findViewById(R.id.btnloc);
        a="Connought Palace , New Delhi";//Here , the location from database will be fetched
        text.setText(a);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri IntentUri = Uri.parse("geo:0,0?q="+a);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, IntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }
}
