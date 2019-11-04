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
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    TextView linkSignup,forgot,redirect;
    EditText txtEmail, txtPassword;
    Button btnLogin;

    ProgressBar circle;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    TextView countdown,alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        linkSignup = (TextView) findViewById(R.id.link_signup);
        txtEmail = (EditText) findViewById(R.id.input_email);
        txtPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        circle=(ProgressBar)findViewById(R.id.progress_circular);
        forgot=(TextView)findViewById(R.id.forgot);
        redirect=(TextView)findViewById(R.id.redirect);
        ImageView imageView=(ImageView)findViewById(R.id.logo);

        Glide.with(LoginActivity.this).load(R.drawable.yeficon).apply(RequestOptions.circleCropTransform()).into(imageView);

        //imageView.startAnimation(rotate);

        imageView.setAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_in));

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeui_forgotpassword();
            }
        });




        linkSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 25);
        myAnim.setInterpolator(interpolator);





        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.startAnimation(myAnim);
                Button b = (Button)v;
                String buttonText = b.getText().toString();
                if(buttonText.trim()=="Get Password Now") {
                    if(txtEmail.getText().toString().trim().isEmpty()){
                        txtEmail.setError("This field is required!");
                    }else {
                        //btnLogin.setEnabled(false);
                        circle.setVisibility(View.VISIBLE);
                        forgot_password(v);
                        //Snackbar.make(v, "No validation errors, continue login", Snackbar.LENGTH_LONG).setAction("Action",null).show();

                    }

                }
                else if(buttonText.trim()=="Login")
                {
                    if(txtEmail.getText().toString().trim().isEmpty()){
                        txtEmail.setError("This field is required!");
                    }else if(!isValidEmail(txtEmail.getText().toString().trim())){
                        txtEmail.setError("This is not a valid email!");
                    }else if(txtPassword.getText().toString().trim().isEmpty()){
                        txtPassword.setError("This field is required!");
                    }else {
                        //btnLogin.setEnabled(false);
                        circle.setVisibility(View.VISIBLE);
                        login();
                        //Snackbar.make(v, "No validation errors, continue login", Snackbar.LENGTH_LONG).setAction("Action",null).show();

                    }
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

               // Toast.makeText(LoginActivity.this, s,Toast.LENGTH_LONG).show();



                try {
                    if (!(s.trim().equals("error"))) {

                        JSONObject Object = new JSONObject(s.trim());
                        String name = Object.getString("name");
                        String user_type = Object.getString("user_type");
                        String status = Object.getString("status");
                        String userid= Object.getString("userid");
                        String email= Object.getString("email");
                        String acnt_status=Object.getString("acnt_status");

                        if (status.trim().equals("Blocked")) {
                            btnLogin.setEnabled(true);
                            circle.setVisibility(View.GONE);
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setCancelable(false)
                                    .setIcon(R.mipmap.ic_launcher_round)
                                    .setTitle("Alert:")
                                    .setMessage("Sorry " + name + ", Your YEF Account is Blocked!\n" +
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

                        }else if (acnt_status.equals("0")) {
                            btnLogin.setEnabled(true);
                            circle.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Account Not Verified!", Toast.LENGTH_LONG).show();
                            enter_otp(name,email,userid);
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

    void enter_otp(String gname,String gemail,String user_id)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verify OTP to Activate Account!");
        builder.setMessage("To activate your Account please check you mail or spam box!");
        builder.setIcon(R.mipmap.ic_launcher_round);

        final View viewInflated = LayoutInflater.from(this).inflate(R.layout.otp_verify_login, (ViewGroup) findViewById(R.id.f1), false);

        EditText otp=viewInflated.findViewById(R.id.otp) ;
        countdown=viewInflated.findViewById(R.id.countdown);
        alert=viewInflated.findViewById(R.id.alert);
        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOTP(gemail,gname);
            }
        });

        TextView textViewemail=viewInflated.findViewById(R.id.email);
        textViewemail.setText(gemail);

        builder.setView(viewInflated);

        builder.setCancelable(false);

        builder.setPositiveButton("Verify", null);
// Set up the buttons
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        //builder.show();


        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(final DialogInterface dialog) {

                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        //Validation check
                        if(otp.getText().toString().trim().isEmpty())
                        {
                            Toast.makeText(LoginActivity.this, "You have not entered any OTP",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            verify_otp(otp.getText().toString().trim(),user_id,gemail,gname);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        mAlertDialog.show();
    }

    void verify_otp(String getotp,String user_id,String email ,String name)
    {
        final Bitmap[] bitmap = new Bitmap[1];
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(LoginActivity.this, "Verifying Otp...", null);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //startActivity(new Intent(Event.this,Event.class));
                if(s.trim().equals("Registered Successfully!"))
                {
                    Toast.makeText(LoginActivity.this, "Account Activated!",Toast.LENGTH_LONG).show();
                    Toast.makeText(LoginActivity.this, "Login Now!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, s,Toast.LENGTH_LONG).show();
                }



            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap[0] = params[0];

                HashMap<String,String> data = new HashMap<>();
                data.put("otp", getotp);
                data.put("v_email",txtEmail.getText().toString().trim());
                data.put("name", name);
                data.put("user_id", user_id);
                String result = rh.sendPostRequest(getString(R.string.url)+"verifyotp_yef.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap[0]);
    }

    void sendOTP(String email_id,String name)
    {
        final Bitmap[] bitmap = new Bitmap[1];
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(LoginActivity.this, "Sending OTP in your mail id...", null);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //startActivity(new Intent(Event.this,Event.class));
                if(s.trim().equals("Registered Successfully!"))
                {
                    Toast.makeText(LoginActivity.this, "OTP sent!",Toast.LENGTH_LONG).show();
                    alert.setVisibility(View.GONE);
                    countdown.setVisibility(View.VISIBLE);
                    startcoundown();
                }
                else
                {
                    Toast.makeText(LoginActivity.this, s,Toast.LENGTH_LONG).show();
                }


            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap[0] = params[0];

                HashMap<String,String> data = new HashMap<>();
                data.put("v_email",email_id);
                data.put("name",name);
                String result = rh.sendPostRequest(getString(R.string.url)+"send_otp_again.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap[0]);
    }

    void startcoundown()
    {
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                countdown.setText("Try resending after " + millisUntilFinished / 1000+" seconds!");
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                alert.setVisibility(View.VISIBLE);
                countdown.setVisibility(View.GONE);
            }

        }.start();
    }


    void changeui_forgotpassword()
    {
        txtPassword.setVisibility(View.GONE);
        btnLogin.setText("Get Password Now");
    }

    void forgot_password(View v) {
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

                // Toast.makeText(LoginActivity.this, s,Toast.LENGTH_LONG).show();
                circle.setVisibility(View.GONE);

                if(s.trim().equals("ESS"))
                {  Toast.makeText(LoginActivity.this, "Password Successfully sent to your mail id!",Toast.LENGTH_LONG).show();
                btnLogin.setVisibility(View.GONE);
                    new CountDownTimer(5000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            redirect.setText("Redirecting.... " + millisUntilFinished/1000+" seconds!");
                        }

                        public void onFinish() {
                            Toast.makeText(LoginActivity.this, "Login Now!",Toast.LENGTH_LONG).show();
                            startActivity(getIntent());
                        }

                    }.start();
                }
                else if(s.trim().equals("ESM"))
                {
                    Snackbar.make(v, "Error in sending mail!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                else if(s.trim().equals("error"))
                {
                    Snackbar.make(v, "Error... Try again later!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }



            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap[0] = params[0];

                HashMap<String,String> data = new HashMap<>();
                //data.put("name", txtName.getText().toString().trim());
                data.put("email", txtEmail.getText().toString().trim());

                String result = rh.sendPostRequest(getString(R.string.url)+"forgot_password.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap[0]);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();*/
    }


}
