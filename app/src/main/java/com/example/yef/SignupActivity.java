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
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;

import static com.example.yef.splash.MyPREFERENCES;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    TextView linkSignin;
    EditText txtName, txtEmail, txtPassword, txtConPassword;

    Button btnSignup;
    LinearLayout linearLayout;
    SharedPreferences sharedpreferences;
    ProgressBar circle;

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
        ImageView imageView=(ImageView)findViewById(R.id.logo);

        circle=(ProgressBar)findViewById(R.id.progress_circular);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        Glide.with(SignupActivity.this).load(R.drawable.yeficon).apply(RequestOptions.circleCropTransform()).into(imageView);

        //imageView.startAnimation(rotate);

        imageView.setAnimation(AnimationUtils.loadAnimation(SignupActivity.this, R.anim.fade_in));

        linearLayout=(LinearLayout)findViewById(R.id.ll);

        linkSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);

        // Use bounce interpolator with amplitude 0.2 and frequency 20
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignup.startAnimation(myAnim);
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

                //loading = ProgressDialog.show(SignupActivity.this, "Creating User...", null);
                circle.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                circle.setVisibility(View.GONE);
                //startActivity(new Intent(Event.this,Event.class));
                if(s.trim().equals("Registered Successfully!"))
                {
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("not_activated",true);
                    editor.putString("v_email", txtEmail.getText().toString().trim());
                    editor.apply();
                    Toast.makeText(SignupActivity.this,"Please Login Now to Activate the account!",Toast.LENGTH_LONG).show();
                   // enter_otp();
                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    Toast.makeText(SignupActivity.this, "Login Now!",Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    Toast.makeText(SignupActivity.this, s,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    Toast.makeText(SignupActivity.this, "Login Now!",Toast.LENGTH_LONG).show();
                    finish();
                }



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

/*
    void enter_otp()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verify OTP to Activate Account!");
        builder.setMessage("To activate your Account please check you mail or spam box!");
        builder.setIcon(R.mipmap.ic_launcher_round);

        final View viewInflated = LayoutInflater.from(this).inflate(R.layout.otp_verify, (ViewGroup) findViewById(R.id.f1), false);

        EditText otp=viewInflated.findViewById(R.id.otp) ;
        TextView alert=viewInflated.findViewById(R.id.alert);

        TextView textViewemail=viewInflated.findViewById(R.id.email);
        textViewemail.setText(txtEmail.getText().toString().trim());

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
                            Toast.makeText(SignupActivity.this, "You have not entered any OTP",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            verify_otp(otp.getText().toString().trim());
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        mAlertDialog.show();
    }

    void verify_otp(String getotp)
    {
        final Bitmap[] bitmap = new Bitmap[1];
        class UploadImage extends AsyncTask<Bitmap,Void,String> {

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = ProgressDialog.show(SignupActivity.this, "Verifying Otp...", null);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //startActivity(new Intent(Event.this,Event.class));
                if(s.trim().equals("Registered Successfully!"))
                {
                    Toast.makeText(SignupActivity.this, s,Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignupActivity.this,LoginActivity.class));
                    Toast.makeText(SignupActivity.this, "Login Now!",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(SignupActivity.this, s,Toast.LENGTH_LONG).show();
                }



            }

            @Override
            protected String doInBackground(Bitmap... params) {
                bitmap[0] = params[0];

                HashMap<String,String> data = new HashMap<>();
                data.put("otp", getotp);
                data.put("v_email",txtEmail.getText().toString().trim());
                String result = rh.sendPostRequest(getString(R.string.url)+"verifyotp_yef.php",data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap[0]);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
/*        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();*/
    }


}
