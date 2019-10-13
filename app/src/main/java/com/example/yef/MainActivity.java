package com.example.yef;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


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

/*        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.admin_link:
//                                Admin page redirecting
                                return true;
                            case R.id.share:
//                                Application share procedure
                                return true;
                            case R.id.rate:
//                                Link to playstore rating page
                                return true;
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.options_menu);
                popupMenu.show();
            }
        });*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.admin_link:
                Toast.makeText(getApplicationContext(),"Admin Login",Toast.LENGTH_LONG).show();
                return true;
            case R.id.share:
                Toast.makeText(getApplicationContext(),"Application share Procedure",Toast.LENGTH_LONG).show();
                return true;
            case R.id.rate:
                Toast.makeText(getApplicationContext(),"Rate App",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
