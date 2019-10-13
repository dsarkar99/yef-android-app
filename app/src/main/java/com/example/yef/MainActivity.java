package com.example.yef;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView titlehome = findViewById(R.id.YEF_text);
    private ImageView menu = findViewById(R.id.menu);
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu.setOnClickListener(new View.OnClickListener() {
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
        });
    }

    public void getToHome(View view){
//        Function to get to home page activity on title text click
    }
}
