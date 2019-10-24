package com.example.yef;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.InetAddress;

public class splash extends AppCompatActivity {
    private static int SPLASH_TIMEOUT = 2000;
    public static final String MyPREFERENCES = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (Build.VERSION.SDK_INT >= 17) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        //imageView.startAnimation(rotate);
/*        Animation anim = AnimationUtils.loadAnimation(splash.this, R.anim.slide_down);
        imageView.setAnimation(anim);*/

        SharedPreferences settings = getSharedPreferences(LoginActivity.MyPREFERENCES, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasloggedin", false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    boolean reachable = InetAddress.getByName("google.com").isReachable(5000);
                  //  if (!reachable) {
                        if (hasLoggedIn) {
                            //Go directly to main activity.
                            try {

                                Intent i = new Intent(splash.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            } catch (Exception e) {
                                Toast.makeText(splash.this, e.toString(), Toast.LENGTH_SHORT).show();
                            }
                            // Stop current activity
                            finish();
                        } else {
                            Intent i = new Intent(splash.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
/*                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(splash.this);
                        alertDialogBuilder.setTitle("Alert!");
                        alertDialogBuilder.setMessage("Unfortunately, YEF Server is down. We are very sorry for the inconvenience caused. Try again later...\n");
                        alertDialogBuilder.setIcon(R.mipmap.ic_launcher);
                        alertDialogBuilder.setCancelable(false);
                        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                                finish();

                            }
                        });
                        alertDialogBuilder.show();
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, SPLASH_TIMEOUT);
    }
}

