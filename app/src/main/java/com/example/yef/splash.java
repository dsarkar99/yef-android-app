package com.example.yef;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

public class splash extends AppCompatActivity {
    private static int SPLASH_TIMEOUT = 2000;
    public static final String MyPREFERENCES = "MyPrefs";
    ProgressBar circle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        if (Build.VERSION.SDK_INT >= 17) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }

        circle=(ProgressBar)findViewById(R.id.progress_circular);

        //imageView.startAnimation(rotate);
/*        Animation anim = AnimationUtils.loadAnimation(splash.this, R.anim.slide_down);
        imageView.setAnimation(anim);*/


        SharedPreferences settings = getSharedPreferences(LoginActivity.MyPREFERENCES, 0);
        //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
        boolean hasLoggedIn = settings.getBoolean("hasloggedin", false);
        String name=settings.getString("name","yes");
        String email=settings.getString("email","yes");


/*        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);*/


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                get_active(email);
            }
        }, SPLASH_TIMEOUT);
    }

    void get_active(String email)
    {
        final Bitmap[] bitmap = new Bitmap[1];
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(splash.this, "", "Please Wait...");
                circle.setVisibility(View.VISIBLE);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                //Toast.makeText(splash.this, s,Toast.LENGTH_LONG).show();
                if(s.equals("[]"))
                {
                    Intent i = new Intent(splash.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                { circle.setVisibility(View.GONE);
                    try{
                        JSONArray jsonArray = new JSONArray(s.trim());
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String active = jsonObject.getString("active");
                        if(active.equals("Y"))
                        {
                            Intent i = new Intent(splash.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else if(active.equals("N"))
                        {
                            Intent i = new Intent(splash.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }

                    }
                    catch (
                            JSONException e) {
                        //loading.dismiss();
                        circle.setVisibility(View.GONE);
                        Toast.makeText(splash.this, e.toString(),Toast.LENGTH_LONG).show();
                    }
                }




            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap[0] = params[0];

                HashMap<String,String> data = new HashMap<>();
                data.put("email", email);
                String result = rh.sendPostRequest(getString(R.string.url)+"verify_active.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap[0]);
    }
}

