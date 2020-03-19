package com.cadmin.myadmin.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cadmin.myadmin.R;
import com.cadmin.myadmin.models.ComplaintModel;

import static com.cadmin.myadmin.ui.MainActivity.mypreference;

public class ViewComplaintActivity extends AppCompatActivity {

    private EditText etComplaint,etArea,etStatus,etOfficerId;
    private ImageView ivComplint;
    private TextView tvUpdate;
    private SharedPreferences sharedpreferences;
    private boolean isAdminLogin = false;
    private ComplaintModel complaintModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_complaint);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ivComplint = (ImageView) findViewById(R.id.ivComplaint);
        etArea = (EditText) findViewById(R.id.tvArea);
        etComplaint = (EditText) findViewById(R.id.tvComplaint);
        etStatus = (EditText) findViewById(R.id.etStatus);
        etOfficerId = (EditText) findViewById(R.id.etOfficer);
        tvUpdate = (TextView) findViewById(R.id.tvUpdate);

        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(complaintModel!=null){
                    Intent intent = (new Intent(ViewComplaintActivity.this,UpdateComplaint.class));
                    intent.putExtra("Complaint",complaintModel);
                    startActivity(intent);
                    finish();
                }


            }
        });
        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("Complaint")){
            complaintModel = (ComplaintModel) intent.getSerializableExtra("Complaint");
            etArea.setText(complaintModel.getArea());
            etComplaint.setText(complaintModel.getComplaint());
            etStatus.setText(complaintModel.getStatus());
            etOfficerId.setText(complaintModel.getOfficerId());

            Glide.with(this)
                    .load(complaintModel.getImageUrl())
                    .into(ivComplint);
        }

        checkAdmin();
    }

    private void checkAdmin() {
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains("IS_ADMIN")) {
            isAdminLogin = (sharedpreferences.getBoolean("IS_ADMIN", false));
        }

        if(isAdminLogin){
         tvUpdate.setVisibility(View.VISIBLE);
        }else {
            tvUpdate.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
