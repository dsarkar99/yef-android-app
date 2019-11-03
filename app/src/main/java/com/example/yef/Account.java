package com.example.yef;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.yef.Model.accountmodelclass;
import com.example.yef.adapter.accounteventadapter;
import com.example.yef.adapter.eventadapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Account extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPreferences;
    String name,email,userid,user_type;
    Bitmap bitmap;

    private RecyclerView recyclerView;
    private List<accountmodelclass> contactList;
    private accounteventadapter mAdapter;

    private RecyclerView recyclerView1;
    private List<accountmodelclass> contactList1;
    private accounteventadapter mAdapter1;
    ProgressBar circle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView pname=(TextView)findViewById(R.id.user_profile_name) ;
        TextView pemail=(TextView)findViewById(R.id.user_email) ;
        TextView pid=(TextView)findViewById(R.id.user_id) ;
        TextView type=(TextView)findViewById(R.id.user_profile_type);

        circle=(ProgressBar)findViewById(R.id.progress_circular);

        ImageView imageView=(ImageView)findViewById(R.id.user_profile_photo) ;


        Glide.with(Account.this).load(R.drawable.yeficon).apply(RequestOptions.circleCropTransform()).into(imageView);

        //imageView.startAnimation(rotate);

        imageView.setAnimation(AnimationUtils.loadAnimation(Account.this, R.anim.fade_in));


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

        recyclerView = findViewById(R.id.recyclerattended);
        contactList = new ArrayList<>();
        mAdapter = new accounteventadapter(Account.this, contactList);

        // white background notification bar
        //whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);



        recyclerView1 = findViewById(R.id.recyclerattending);
        contactList1 = new ArrayList<>();
        mAdapter1 = new accounteventadapter(Account.this, contactList1);

        // white background notification bar
        //whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerView1.setLayoutManager(mLayoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL, 36));
        recyclerView1.setAdapter(mAdapter1);

        show_attending_events();

    }

    void show_attended_events()
    {
        @SuppressLint("StaticFieldLeak")
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            private ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //loading = ProgressDialog.show(Account.this, "", "Please Wait...");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //Toast.makeText(Account.this, s, Toast.LENGTH_LONG).show();

                    if(Objects.equals(s.trim(), "error"))
                    {
                        //Toast.makeText(MainActivity.this,"Request Timed out!\n" + "Refresh Now..",Toast.LENGTH_LONG).show();
                        Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "Request Timed out!" + "Refresh Now..", Snackbar.LENGTH_LONG);
                        sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                        sb.show();
                        contactList.clear();
                        mAdapter.notifyDataSetChanged();
                        //loading.dismiss();
                        circle.setVisibility(View.GONE);
                    }
                    else if(Objects.equals(s.trim(), "")) {
                        Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "Unable to fetch data!", Snackbar.LENGTH_LONG);
                        sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                        sb.show();
                        //Toast.makeText(MainActivity.this, "Unable to fetch data!", Toast.LENGTH_LONG).show();
                        contactList.clear();
                        mAdapter.notifyDataSetChanged();
                        //loading.dismiss();
                        circle.setVisibility(View.GONE);
                    }
                    else if(Objects.equals(s.trim(), "[]")) {
                        contactList.clear();
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(Account.this, "You have not attended any Events uptill now!", Toast.LENGTH_LONG).show();
/*                        Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "You have not attended any Events uptill now!", Snackbar.LENGTH_LONG);
                        sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                        sb.show();*/
                        //Snackbar.make(findViewById(R.id.constraintlayoutroot), "Sorry, No events on "+selectedGridDate+"!", Snackbar.LENGTH_LONG).show();
                        //loading.dismiss();
                        circle.setVisibility(View.GONE);
                    }
                    else
                    {
                        List<accountmodelclass> items = new Gson().fromJson(s, new TypeToken<List<accountmodelclass>>() {
                        }.getType());


                        // adding contacts to contacts list
                        contactList.clear();
                        contactList.addAll(items);

                        //loading.dismiss();
                        circle.setVisibility(View.GONE);

                        final LayoutAnimationController controller =
                                AnimationUtils.loadLayoutAnimation(Account.this, R.anim.layout_animation_up_to_down);

                        recyclerView.setLayoutAnimation(controller);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.scheduleLayoutAnimation();
                    }

                //Toast.makeText(SellingPortal.this, s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap = params[0];

                HashMap<String,String> data = new HashMap<>();
                data.put("email",email);
                String result = rh.sendPostRequest(getString(R.string.url)+"get_completed_events.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    void show_attending_events()
    {
        @SuppressLint("StaticFieldLeak")
        class UploadImage extends AsyncTask<Bitmap,Void,String> {
            private ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                //loading = ProgressDialog.show(Account.this, "", "Please Wait...");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //Toast.makeText(Account.this, s, Toast.LENGTH_LONG).show();
                    if(Objects.equals(s.trim(), "error"))
                    {
                        //Toast.makeText(MainActivity.this,"Request Timed out!\n" + "Refresh Now..",Toast.LENGTH_LONG).show();
                        Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "Request Timed out!" + "Refresh Now..", Snackbar.LENGTH_LONG);
                        sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                        sb.show();
                        contactList1.clear();
                        mAdapter1.notifyDataSetChanged();
                        //loading.dismiss();
                        circle.setVisibility(View.GONE);
                    }
                    else if(Objects.equals(s.trim(), "")) {
                        Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "Unable to fetch data!", Snackbar.LENGTH_LONG);
                        sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                        sb.show();
                        //Toast.makeText(MainActivity.this, "Unable to fetch data!", Toast.LENGTH_LONG).show();
                        contactList1.clear();
                        mAdapter1.notifyDataSetChanged();
                        //loading.dismiss();
                        circle.setVisibility(View.GONE);
                    }
                    else if(Objects.equals(s.trim(), "[]")) {
                        contactList1.clear();
                        mAdapter1.notifyDataSetChanged();
                        Toast.makeText(Account.this, "You are currently not attending any Events!", Toast.LENGTH_LONG).show();
/*                        Snackbar sb = Snackbar.make(findViewById(R.id.constraintlayoutroot), "You are currently not attending any Events!", Snackbar.LENGTH_LONG);
                        sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.snackbarbg));
                        sb.show();*/
                        //Snackbar.make(findViewById(R.id.constraintlayoutroot), "Sorry, No events on "+selectedGridDate+"!", Snackbar.LENGTH_LONG).show();
                        //loading.dismiss();
                        circle.setVisibility(View.GONE);
                    }
                    else
                    {
                        List<accountmodelclass> items = new Gson().fromJson(s, new TypeToken<List<accountmodelclass>>() {
                        }.getType());


                        // adding contacts to contacts list
                        contactList1.clear();
                        contactList1.addAll(items);

                        //loading.dismiss();
                        circle.setVisibility(View.GONE);

                        final LayoutAnimationController controller =
                                AnimationUtils.loadLayoutAnimation(Account.this, R.anim.layout_animation_up_to_down);

                        recyclerView1.setLayoutAnimation(controller);
                        mAdapter1.notifyDataSetChanged();
                        recyclerView1.scheduleLayoutAnimation();
                    }

                show_attended_events();

                //Toast.makeText(SellingPortal.this, s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap = params[0];

                HashMap<String,String> data = new HashMap<>();
                data.put("email",email);
                String result = rh.sendPostRequest(getString(R.string.url)+"get_completed_events.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Account.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
