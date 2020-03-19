package com.cadmin.myadmin;

import android.app.ProgressDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progress=new ProgressDialog(this);
        progress.setMessage("Please wait...");
    }

    public void showProgress(){
        progress.show();
    }

    public void hideProgress(){
        progress.cancel();
    }
}
