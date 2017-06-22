package com.example.sid.marwadishaadi.Settings;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sid.marwadishaadi.About_Us.AboutUsActivity;
import com.example.sid.marwadishaadi.Blocked_Members.BlockedActivity;
import com.example.sid.marwadishaadi.Contact_Us.ContactUsActivity;
import com.example.sid.marwadishaadi.Faq.FaqActivity;
import com.example.sid.marwadishaadi.Login.LoginActivity;
import com.example.sid.marwadishaadi.Payment_Policy.PaymentPolicyActivity;
import com.example.sid.marwadishaadi.Privacy_Policy.PrivacyPolicyActivity;
import com.example.sid.marwadishaadi.R;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {


    protected SwitchCompat notifications;
    protected TextView reset_pass;
    protected TextView deactivate_acc;
    protected TextView delete_acc;
    protected TextView blocked;
    protected TextView logout;
    protected TextView faq;
    protected TextView aboutus;
    protected TextView privacypolicy;
    protected TextView contactus;
    protected TextView paymentpolicy;
    protected LinearLayout morelinearlayout;
    protected TextView more;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle("Settings");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        faq = (TextView) findViewById(R.id.faq);
        aboutus = (TextView) findViewById(R.id.aboutus);
        privacypolicy= (TextView) findViewById(R.id.privacypolicy);
        paymentpolicy = (TextView) findViewById(R.id.paymentpolicy);
        contactus= (TextView) findViewById(R.id.contactus);

        faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,FaqActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,AboutUsActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,PrivacyPolicyActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });

        paymentpolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingsActivity.this,PaymentPolicyActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });

        contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsActivity.this,ContactUsActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            }
        });


        notifications = (SwitchCompat) findViewById(R.id.switchNot);
        reset_pass = (TextView) findViewById(R.id.reset_password);
        deactivate_acc = (TextView) findViewById(R.id.deactivate_acc);
        delete_acc = (TextView) findViewById(R.id.delete_acc);
        blocked = (TextView) findViewById(R.id.blocked);
        logout = (TextView) findViewById(R.id.Logout);
        more = (TextView) findViewById(R.id.more);
        morelinearlayout = (LinearLayout) findViewById(R.id.morelayout);


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (morelinearlayout.getVisibility() == View.GONE){
                    morelinearlayout.setVisibility(View.VISIBLE);
                }else{
                    morelinearlayout.setVisibility(View.GONE);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder logout_user = new AlertDialog.Builder(SettingsActivity.this);
                logout_user.setMessage("Are you sure you want to logout ?")
                        .setIcon(R.drawable.setting_logout)
                        .setCancelable(true)
                        .setTitle("Logout")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // perform check
                                LoginManager.getInstance().logOut();
                                AccessToken.setCurrentAccessToken(null);
                                SharedPreferences sharedPre=getSharedPreferences("userinfo",MODE_PRIVATE);
                                SharedPreferences.Editor editor=sharedPre.edit();
                                editor.putBoolean("isLoggedIn",false);
                                editor.putString("email","");
                                editor.putString("password","");
                                editor.putString("customer_id","");
                                editor.commit();
                                Intent i=new Intent(SettingsActivity.this, LoginActivity.class);
                                startActivity(i);

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alertbox = logout_user.create();
                alertbox.setTitle("Logout");
                alertbox.show();
            }
        });

        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View reset_view = getLayoutInflater().inflate(R.layout.reset_dialog,null);
                AlertDialog.Builder reset = new AlertDialog.Builder(SettingsActivity.this);
                reset.setTitle("Reset Password");
                reset.setView(reset_view);

                final EditText oldpass = (EditText) reset_view.findViewById(R.id.user_old_pass);
                final EditText newpass = (EditText) reset_view.findViewById(R.id.user_new_password);
                Button resetbutton = (Button) reset_view.findViewById(R.id.user_reset);
                final TextView call_us = (TextView) reset_view.findViewById(R.id.call_us);

                resetbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String user_old_pass = oldpass.getText().toString();
                        String user_new_pass = newpass.getText().toString();

                        Toast.makeText(SettingsActivity.this, "yayay", Toast.LENGTH_SHORT).show();
                    }
                });

                call_us.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + call_us.getText().toString()));//change the number
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getApplicationContext(),"Permission for Call Denied!",Toast.LENGTH_LONG).show();
                            return;
                        }else{
                            AlertDialog.Builder discarduser = new AlertDialog.Builder(SettingsActivity.this);
                            discarduser.setMessage("Do you want to call " + call_us.getText().toString() + " ? ")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            startActivity(callIntent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            // setting up dialog box
                            AlertDialog alertbox = discarduser.create();
                            alertbox.setTitle("Contact Us");
                            alertbox.show();
                        }
                    }
                });

                AlertDialog resetbox = reset.create();
                resetbox.show();
            }
        });

        deactivate_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder deactivate = new AlertDialog.Builder(SettingsActivity.this);
                    deactivate.setMessage("Deactivating will temporarily disable your account.")
                            .setIcon(R.drawable.setting_blocked)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(SettingsActivity.this, "cool!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(SettingsActivity.this, "ohh whyyy", Toast.LENGTH_SHORT).show();
                                }
                            });

                AlertDialog box = deactivate.create();
                box.setTitle("Deactivate Account ?");
                box.show();
            }
        });

        delete_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder delete = new AlertDialog.Builder(SettingsActivity.this);
                delete.setMessage("Deleting will permanently remove your account.")
                        .setIcon(R.drawable.setting_delete)
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingsActivity.this, "cool!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingsActivity.this, "ohh whyyy", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog box = delete.create();
                box.setTitle("Delete Account ?");
                box.show();
            }
        });

        blocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SettingsActivity.this,BlockedActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
