package com.oadevelopers.winbazi.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.oadevelopers.winbazi.R;
import com.oadevelopers.winbazi.common.Config;
import com.oadevelopers.winbazi.session.SessionManager;


public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000;
    private boolean mIsBackButtonPressed;
    private SessionManager sessionManager;
    private String force_update, whats_new, update_date, latest_version_name, update_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sessionManager = new SessionManager(getApplicationContext());
        MobileAds.initialize(SplashActivity.this, Config.AD_APP_ID);

        printHashKey();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mIsBackButtonPressed) {
                    if (sessionManager.isLoggedIn()) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), IntroActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }

        }, SPLASH_DURATION);
    }

    private void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.winbazi.winbazi", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // set the flag to true so the next activity won't start up
        mIsBackButtonPressed = true;
        super.onBackPressed();

    }

}
