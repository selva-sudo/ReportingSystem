package com.cadmin.myadmin.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cadmin.myadmin.BaseActivity;
import com.cadmin.myadmin.R;
import com.cadmin.myadmin.models.ComplaintModel;
import com.cadmin.myadmin.models.NewOfficer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateComplaint extends BaseActivity {

    private EditText etComplaint, etArea;
    private Spinner spOfficer, spStatus;
    private ImageView ivComplaint;
    private TextView tvSave;
    private String status,officerId;
    private List<NewOfficer> mOfficerList;
    private DatabaseReference mDatabase;
    private ArrayAdapter statusAdapter,officersidAdapter;
    private ArrayList<String> officersIdList;
    private ComplaintModel complaintModel;
    private List<String> statusArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_complaint);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etArea = (EditText) findViewById(R.id.tvArea);
        etComplaint = (EditText) findViewById(R.id.tvComplaint);
        spOfficer = (Spinner) findViewById(R.id.spOfficer);
        spStatus = (Spinner) findViewById(R.id.spStatus);
        ivComplaint = (ImageView) findViewById(R.id.ivComplaint);
        tvSave = (TextView) findViewById(R.id.TvSave);
        mOfficerList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        statusArray = new ArrayList<>();
        //String[] statusArray = {"New", "Assigned", "Rejected", "Pending", "Completed"};
        statusArray.add("New");
        statusArray.add("Assigned");
        statusArray.add("Rejected");
        statusArray.add("Pending");
        statusArray.add("Completed");

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = statusArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spOfficer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                officerId = officersIdList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        officersIdList = new ArrayList<>();
        statusAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, statusArray);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spStatus.setAdapter(statusAdapter);

        officersidAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, officersIdList);
        officersidAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spOfficer.setAdapter(officersidAdapter);

        getofficers();

        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                complaintModel.setOfficerId(officerId);
                complaintModel.setStatus(status);
                mDatabase.child("Complaints").child(complaintModel.getComplaintId()).setValue(complaintModel)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                hideProgress();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hideProgress();
                                Log.e("Error",e.toString());
                                Toast.makeText(UpdateComplaint.this, "Error occurred please try again later", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        Intent intent = getIntent();
        if(intent!=null && intent.hasExtra("Complaint")){
            complaintModel = (ComplaintModel) intent.getSerializableExtra("Complaint");
            etArea.setText(complaintModel.getArea());
            etComplaint.setText(complaintModel.getComplaint());
            spStatus.setSelection(statusArray.indexOf(complaintModel.getStatus()));

            Glide.with(this)
                    .load(complaintModel.getImageUrl())
                    .into(ivComplaint);
        }

    }

    private void getofficers() {
        showProgress();
        mDatabase.child("Officers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                hideProgress();
                mOfficerList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    NewOfficer newOfficer = postSnapshot.getValue(NewOfficer.class);
                    mOfficerList.add(newOfficer);

                    // here you can access to name property like university.name

                }
                officersIdList.clear();
                for(int i=0;i<mOfficerList.size();i++){
                    officersIdList.add(mOfficerList.get(i).id);
                }
                officersidAdapter.notifyDataSetChanged();
                spOfficer.setSelection(officersIdList.indexOf(complaintModel.getOfficerId()));

                //mRvComplaints.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgress();
                Toast.makeText(UpdateComplaint.this, "The read failed: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

   /* @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()){
            case R.id.spOfficer:
                break;
            case R.id.spStatus:

                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}
