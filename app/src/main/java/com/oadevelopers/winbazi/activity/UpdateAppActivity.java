package com.oadevelopers.winbazi.activity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.oadevelopers.winbazi.BuildConfig;
import com.oadevelopers.winbazi.R;

public class UpdateAppActivity extends AppCompatActivity {

    private static String TAG = "UpdateApp";
    private int MULTIPLE_PERMISSIONS = 1;

    private String updateURL;
    private String updatedOn;
    private String whatsNewData;
    private String isForceUpdate = "false";
    private String latestVersion;
    private String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_SMS", "android.permission.RECEIVE_SMS"};

    private ProgressDialog bar;
    private Button later;
    private Button update;
    private TextView updateDate;
    private TextView whatsNew;
    private TextView newVersion;
    private TextView forceUpdateNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_app);


        this.updateDate = (TextView) findViewById(R.id.date);
        this.newVersion = (TextView) findViewById(R.id.version);
        this.whatsNew = (TextView) findViewById(R.id.whatsnew);
        this.forceUpdateNote = (TextView) findViewById(R.id.forceUpdateNote);
        this.later = (Button) findViewById(R.id.laterButton);
        this.update = (Button) findViewById(R.id.updateButton);

        this.isForceUpdate = getIntent().getStringExtra("forceUpdate");
        this.updateURL = getIntent().getStringExtra("updateURL");
        this.updatedOn = getIntent().getStringExtra("updateDate");
        this.whatsNewData = getIntent().getStringExtra("whatsNew");
        this.latestVersion = getIntent().getStringExtra("latestVersionName");

        this.updateDate.setText("Updated On: " + this.updatedOn);
        this.newVersion.setText("New Version: v" + BuildConfig.VERSION_NAME);

        if (whatsNewData != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                this.whatsNew.setText(Html.fromHtml(whatsNewData, Html.FROM_HTML_MODE_LEGACY));
            } else {
                this.whatsNew.setText(Html.fromHtml(whatsNewData));
            }
        }

        if (this.isForceUpdate.equals("1")) {
            this.later.setVisibility(View.GONE);
            this.forceUpdateNote.setVisibility(View.VISIBLE);
        }

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissions();
                openWebPage(updateURL);
            }
        });

        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateAppActivity.this.finish();
            }
        });

    }

    public void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                webpage = Uri.parse("http://" + url);
            }
            Intent myIntent = new Intent(Intent.ACTION_VIEW, webpage);
            startActivity(myIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No application can handle this request. Please install a web browser or check your URL.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private boolean checkPermissions() {
        List arrayList = new ArrayList();
        for (String str : this.permissions) {
            if (ContextCompat.checkSelfPermission(this, str) != 0) {
                arrayList.add(str);
            }
        }
        if (arrayList.isEmpty()) {
            return true;
        }
        ActivityCompat.requestPermissions(this, (String[]) arrayList.toArray(new String[arrayList.size()]), this.MULTIPLE_PERMISSIONS);
        return false;
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == this.MULTIPLE_PERMISSIONS && iArr.length > 0) {
            i = iArr[0];
        }
    }


}
