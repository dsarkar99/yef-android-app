package com.example.yef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Account extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    String name,email,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView pname=(TextView)findViewById(R.id.user_profile_name) ;
        TextView pemail=(TextView)findViewById(R.id.user_email) ;
        TextView pid=(TextView)findViewById(R.id.user_id) ;


        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences settings = getSharedPreferences(LoginActivity.MyPREFERENCES, 0);

        email = settings.getString("email", "yes");
        userid = settings.getString("userid", "yes");
        name=settings.getString("name", "yes");

        pname.setText(name);
        pid.setText(userid);
        pemail.setText(email);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Account.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
