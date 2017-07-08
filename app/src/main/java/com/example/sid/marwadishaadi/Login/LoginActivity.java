package com.example.sid.marwadishaadi.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.Dashboard.DashboardActivity;
import com.example.sid.marwadishaadi.Forgot_Password.ForgotPasswordActivity;
import com.example.sid.marwadishaadi.R;
import com.example.sid.marwadishaadi.Signup.SignupActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_gender;
import static com.example.sid.marwadishaadi.Login.LoginActivity.customer_id;

public class LoginActivity extends AppCompatActivity {
    public ProgressDialog dialog;
    protected EditText login_email;
    protected EditText login_pass;
    protected Button login;
    protected TextView forgot;
    public static String customer_id = "A1001";
    public static String customer_gender;
    protected TextView signup;
    protected LoginButton fblogin;
    private String str = "";
    private  String email,pass;
    private FirebaseAnalytics mFirebaseAnalytics;

    CallbackManager callbackManager;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        AndroidNetworking.initialize(getApplicationContext());
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.setCanceledOnTouchOutside(false);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        callbackManager = CallbackManager.Factory.create();


        login_email = (EditText) findViewById(R.id.login_email);
        login_pass = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login);
        fblogin = (LoginButton) findViewById(R.id.fb_login_button);
        login = (Button) findViewById(R.id.login);

        fblogin = (LoginButton) findViewById(R.id.fb_login_button);
        fblogin.setReadPermissions(Arrays.asList("email"));
        fblogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        Log.d("object", object.toString());

                        try {
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String gender = object.getString("gender");
                            String name = object.getString("name");

                            // check must be performed here
                            //String email = object.getString("email");
                            String birthday = object.getString("birthday");
                            Toast.makeText(getApplicationContext(), first_name + last_name + gender + birthday, Toast.LENGTH_LONG).show();

                            // MUST GO TO dashboard
                            Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,about,birthday,middle_name,first_name,last_name,email,gender,name,relationship_status");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }


        });

        forgot = (TextView) findViewById(R.id.forgot_link);
        signup = (TextView) findViewById(R.id.signup_link);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Forgot", "button");
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // analytics
                Analytics_Util.logAnalytic(mFirebaseAnalytics, "Signup", "button");
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {

                                         email = login_email.getText().toString();
                                         pass = login_pass.getText().toString();

                                         if (EmailChecker(email)&!pass.trim().isEmpty()) {
                                             pass = HashConverter(pass);
                                             dialog.setMessage("Please Wait...");
                                             dialog.show();
                                             new BackGround().execute("email", email, pass);
                                             Analytics_Util.logAnalytic(mFirebaseAnalytics, "Login", "button");
                                         }
                                         else if((email.trim().contains("M")|email.trim().contains("m")|email.trim().contains("a")|email.trim().contains("A")|email.trim().contains("o")|email.trim().contains("O")|email.trim().contains("J")|email.trim().contains("j")|email.trim().contains("K")|email.trim().contains("k"))&!pass.trim().isEmpty()){
                                             dialog.setMessage("Please Wait...");
                                             dialog.show();
                                             pass = HashConverter(pass);
                                             char charEmail= email.charAt(0);
                                             char character=email.charAt(0);
                                             if((int)charEmail<123 & (int)charEmail>96){
                                                 charEmail=(char)((int)charEmail-32);
                                                 email=email.replace(character,charEmail);
                                             }
                                             //Toast.makeText(LoginActivity.this, "User ID is **************** "+ email, Toast.LENGTH_SHORT).show();
                                             new BackGround().execute("user_id", email, pass);
                                             Analytics_Util.logAnalytic(mFirebaseAnalytics, "Login", "button");
                                         }
                                         else {
                                             Toast.makeText(LoginActivity.this, "Enter right email address or userId", Toast.LENGTH_SHORT).show();
                                         }
                                     }
                                 }
        );
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    boolean EmailChecker(String s) {
        String EMAIL_REGEX = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    //    anita.k@makindia.com
    public static String HashConverter(String pswrd) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(pswrd.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length(

            ) < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }


    private class BackGround extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            if(strings[0].contains("email")) {
                AndroidNetworking.post("http://208.91.199.50:5000/checkLogin/{check}")
                        .addPathParameter("check", "email")
                        .addBodyParameter("email", strings[1])
                        .addBodyParameter("password", strings[2])
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    str = response.getString(0);
                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(dialog.isShowing()) {
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                    if (str.contains("success")) {
                                        customer_id = response.getString(1);
                                        customer_gender = response.getString(2);
                                        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor=saved_values.edit();
                                        editor.putString("customer_id", customer_id);
                                        editor.putString("gender", customer_gender);
                                        editor.putBoolean("isLoggedIn",true);
                                        JSONArray communityArray=response.getJSONArray(3);
                                        for(int i=0;i<5;i++){
                                            editor.putString(communityArray.getJSONArray(i).getString(0),communityArray.getJSONArray(i).getString(1));
                                        }
                                        editor.apply();
                                        Log.e(TAG, "onResponse: --community and response is "+saved_values.getString("Agarwal",null));
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    else
                                    {Toast.makeText(getApplicationContext(), "Please enter correct email address or password", Toast.LENGTH_LONG).show();}
                                }catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                Log.d(TAG, "onResponse: ----Network Error in Email Login" + error.toString());
                                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else {
                AndroidNetworking.post("http://208.91.199.50:5000/checkLogin/{check}")
                        .addPathParameter("check", "id")
                        .addBodyParameter("email", strings[1])
                        .addBodyParameter("password", strings[2])
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {

                            @Override
                            public void onResponse(JSONArray response) {
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                try {
                                    Log.e(TAG, "onResponse: response is ------------- " + response.toString());
                                    str = response.getString(0);
                                    if (str.contains("success")) {
                                        customer_id = response.getString(1);
                                        customer_gender = response.getString(2);
                                        SharedPreferences saved_values = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor editor = saved_values.edit();
                                        editor.putString("customer_id", customer_id);
                                        editor.putString("gender", customer_gender);
                                        editor.putBoolean("isLoggedIn",true);
                                        JSONArray communityArray=response.getJSONArray(3);
                                        for(int i=0;i<5;i++){
                                            editor.putString(communityArray.getJSONArray(i).getString(0),communityArray.getJSONArray(i).getString(1));
                                        }
                                        editor.apply();
                                        LoginActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Intent intent=new Intent(getApplicationContext(),DashboardActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Please enter correct email address or password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                Log.d(TAG, "onResponse: ----Network Error in User ID Login" + error.toString());
                                Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

