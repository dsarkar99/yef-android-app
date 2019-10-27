package com.example.yef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

public class Account extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    String name,email,userid,user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView pname=(TextView)findViewById(R.id.user_profile_name) ;
        TextView pemail=(TextView)findViewById(R.id.user_email) ;
        TextView pid=(TextView)findViewById(R.id.user_id) ;
        TextView type=(TextView)findViewById(R.id.user_profile_type);


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences settings = getSharedPreferences(LoginActivity.MyPREFERENCES, 0);

        email = settings.getString("email", "yes");
        userid = settings.getString("userid", "yes");
        name=settings.getString("name", "yes");
        user_type=settings.getString("user_type", "yes");

        pname.setText(name);
        pid.setText(userid);
        pemail.setText(email);
        type.setText(user_type);

        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.faback);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Account.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
