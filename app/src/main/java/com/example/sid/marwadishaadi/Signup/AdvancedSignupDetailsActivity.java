package com.example.sid.marwadishaadi.Signup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.sid.marwadishaadi.Analytics_Util;
import com.example.sid.marwadishaadi.Otp_Verification.Otp_VerificationActivity;
import com.example.sid.marwadishaadi.R;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AdvancedSignupDetailsActivity extends AppCompatActivity implements StepperLayout.StepperListener{

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private StepperLayout mStepperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_advanced__signup__details);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mStepperLayout = (StepperLayout) findViewById(R.id.stepperLayout);
        mStepperLayout.setAdapter(new Signup_Additional_Info_StepperAdapter(getSupportFragmentManager(), this));
        mStepperLayout.setListener(this);
    }

    @Override
    public void onCompleted(View completeButton) {

        // analytics
        Analytics_Util.logAnalytic(mFirebaseAnalytics,"Signup Complete","button");

        Intent i = new Intent(AdvancedSignupDetailsActivity.this,Otp_VerificationActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    @Override
    public void onError(VerificationError verificationError) {

    }

    @Override
    public void onStepSelected(int newStepPosition) {

    }

    @Override
    public void onReturn() {
        Intent i = new Intent(AdvancedSignupDetailsActivity.this,SignupDetailsActivity.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
    }
}
