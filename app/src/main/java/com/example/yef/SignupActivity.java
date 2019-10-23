package com.example.yef;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView linkSignin;
    EditText txtName, txtEmail, txtPassword, txtConPassword;

    Button btnSignup;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        linkSignin = (TextView) findViewById(R.id.link_signin);
        txtName = (EditText) findViewById(R.id.txt_name);
        txtEmail = (EditText) findViewById(R.id.txt_email);
        txtPassword = (EditText) findViewById(R.id.txt_password);
        txtConPassword = (EditText) findViewById(R.id.txt_confirm_pwd);
        btnSignup = (Button) findViewById(R.id.btn_signup);

        linearLayout=(LinearLayout)findViewById(R.id.ll);

        linkSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtName.getText().toString().trim().isEmpty()){
                    txtName.setError("This field is required");
                }else if(txtEmail.getText().toString().trim().isEmpty()){
                    txtEmail.setError("This filed is required");
                }else if(!isValidEmail(txtEmail.getText().toString().trim())){
                    txtEmail.setError("Not a valid email");
                }else if(txtPassword.getText().toString().trim().isEmpty()){
                    txtPassword.setError("This field is required");
                }else if(txtConPassword.getText().toString().trim().isEmpty()){
                    txtConPassword.setError("This field is required");
                }else if(txtPassword.getText().toString().trim().compareTo(txtConPassword.getText().toString().trim()) != 0){
                    txtConPassword.setError("Not matched with the Password");
                }else{
                    createuser();
                    //Snackbar.make(v, "No validation errors, continue signup", Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    void  createuser()
    {
        final Bitmap[] bitmap = new Bitmap[1];
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(SignupActivity.this, "Creating User...", null);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //startActivity(new Intent(Event.this,Event.class));
                Toast.makeText(SignupActivity.this, s,Toast.LENGTH_LONG).show();
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                Toast.makeText(SignupActivity.this, "Login Now!",Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap[0] = params[0];

                HashMap<String,String> data = new HashMap<>();
                data.put("name", txtName.getText().toString().trim());
                data.put("email", txtEmail.getText().toString().trim());
                data.put("pass", txtPassword.getText().toString());
                String result = rh.sendPostRequest(getString(R.string.url)+"signup_yef.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap[0]);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
