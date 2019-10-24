package com.example.yef;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    TextView linkSignup;
    EditText txtEmail, txtPassword;
    Button btnLogin;

    ProgressBar circle;

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        linkSignup = (TextView) findViewById(R.id.link_signup);
        txtEmail = (EditText) findViewById(R.id.input_email);
        txtPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        circle=(ProgressBar)findViewById(R.id.progress_circular);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtEmail.getText().toString().trim().isEmpty()){
                    txtEmail.setError("This field is required!");
                }else if(!isValidEmail(txtEmail.getText().toString().trim())){
                    txtEmail.setError("This is not a valid email!");
                }else if(txtPassword.getText().toString().trim().isEmpty()){
                    txtPassword.setError("This field is required!");
                }else{
                    btnLogin.setEnabled(false);
                    circle.setVisibility(View.VISIBLE);
                    login();
                    //Snackbar.make(v, "No validation errors, continue login", Snackbar.LENGTH_LONG).setAction("Action",null).show();

                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target){
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    void login() {
        final Bitmap[] bitmap = new Bitmap[1];
        @SuppressLint("StaticFieldLeak")
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Toast.makeText(LoginActivity.this, inputmno.getText().toString().trim(),Toast.LENGTH_LONG).show();
                // Toast.makeText(LoginActivity.this, inputPassword.getText().toString().trim(),Toast.LENGTH_LONG).show();
                //loading = ProgressDialog.show(LoginActivity.this, "", "Please Wait...");
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                if(s.length()>=16 && s.length()<28)
                { Toast.makeText(LoginActivity.this, s,Toast.LENGTH_LONG).show();}
                else if(s.length()<16)
                {
                    Toast.makeText(LoginActivity.this, s,Toast.LENGTH_LONG).show();}

                try {
                    if (!(s.trim().equals("error"))) {

                        JSONObject Object = new JSONObject(s.trim());
                        String name = Object.getString("name");
                        String user_type = Object.getString("user_type");
                        String status = Object.getString("status");
                        String userid= Object.getString("userid");
                        String email= Object.getString("email");

                        if (s.trim().equals("Blocked")) {
                            btnLogin.setEnabled(true);
                            circle.setVisibility(View.GONE);
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setCancelable(false)
                                    .setIcon(R.mipmap.ic_launcher_round)
                                    .setTitle("Alert:")
                                    .setMessage("Sorry " + name + ", Your Account is Blocked!\n" +
                                            "CONTACT ADMIN NOW")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();

                        } else if (s.trim().equals("WPOEI")) {
                            btnLogin.setEnabled(true);
                            circle.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "User not found!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                            finish();
                        } else if (s.trim().equals("401")) {
                            btnLogin.setEnabled(true);
                            circle.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Wrong Password or Email id!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                            finish();
                        }
                        /*else if(Objects.equals(approval.trim(), ""))
                        {   progressBar.setVisibility(View.GONE);
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setCancelable(false)
                                    .setIcon(R.mipmap.ic_launcher_round)
                                    .setTitle("Alert:")
                                    .setMessage("Sorry "+name+", Your Account is not yet Approved by the Admin!\n"+
                                            "You can try again later or CONTACT ADMIN NOW")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setCancelable(false)
                                    .create()
                                    .show();

                        }*/
                        else {
                            btnLogin.setEnabled(true);
                            circle.setVisibility(View.GONE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putBoolean("hasloggedin",true);
                            editor.putString("name", name);
                            editor.putString("user_type", user_type);
                            editor.putString("userid", userid);
                            editor.putString("email", email);
                            editor.apply();

                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
                        }


                    } else {
                        btnLogin.setEnabled(true);
                        circle.setVisibility(View.GONE);
                        //Snackbar.make(linearLayout, "Authentication Failed! Please check your password and userid and Try Again...", Snackbar.LENGTH_LONG).setAction("Action",null).show();
                        Toast.makeText(LoginActivity.this, "Authentication Failed! Please check your password and email and Try Again...", Toast.LENGTH_LONG).show();
                    }
                }
                    catch (JSONException e) {
                        btnLogin.setEnabled(true);
                        circle.setVisibility(View.GONE);
                        //Toast.makeText(LoginActivity.this, e.toString(),Toast.LENGTH_LONG).show();
                    }

            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap[0] = params[0];

                HashMap<String,String> data = new HashMap<>();
                //data.put("name", txtName.getText().toString().trim());
                data.put("email", txtEmail.getText().toString().trim());
                data.put("pass", txtPassword.getText().toString());

                String result = rh.sendPostRequest(getString(R.string.url)+"signin_yef.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap[0]);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
