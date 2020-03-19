package com.cadmin.myadmin.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cadmin.myadmin.BaseActivity;
import com.cadmin.myadmin.R;
import com.cadmin.myadmin.models.NewOfficer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddOfficerActivity extends BaseActivity {

    private EditText etId,etName,etPhone,etEmail,etAddres,etArea;
    private DatabaseReference mDatabase;
    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_officer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        etAddres = (EditText)findViewById(R.id.address);
        etId = (EditText)findViewById(R.id.id);
        etName = (EditText)findViewById(R.id.name);
        etPhone = (EditText)findViewById(R.id.phone);
        etEmail = (EditText)findViewById(R.id.email);
        etArea = (EditText)findViewById(R.id.area);
        save = (Button) findViewById(R.id.btnSave);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid()) {
                    addofficer();
                }
            }


        });

    }

    private boolean valid() {
        if(!(etId.getText().toString().length()>0)){
            Toast.makeText(AddOfficerActivity.this, "Please add officer name", Toast.LENGTH_LONG).show();

            return false;
        }
        if(!(etName.getText().toString().length()>0)){
            Toast.makeText(AddOfficerActivity.this, "Please add officer name", Toast.LENGTH_LONG).show();

            return false;
        }
        if(!(etPhone.getText().toString().length()>0)){
            Toast.makeText(AddOfficerActivity.this, "Please enter valid phone number", Toast.LENGTH_LONG).show();

            return false;
        }
        if(!(etPhone.getText().toString().length()==10)){
            Toast.makeText(AddOfficerActivity.this, "Please enter valid phone number", Toast.LENGTH_LONG).show();

            return false;
        }
        if(!(etEmail.getText().toString().length()>0)){
            Toast.makeText(AddOfficerActivity.this, "Please enter email", Toast.LENGTH_LONG).show();

            return false;
        }
        if(!(etAddres.getText().toString().length()>0)){
            Toast.makeText(AddOfficerActivity.this, "Please enter address", Toast.LENGTH_LONG).show();

            return false;
        }
        if(!(etArea.getText().toString().length()>0)){
            Toast.makeText(AddOfficerActivity.this, "Please enter area", Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }

    private void addofficer() {
        showProgress();
        final NewOfficer newOfficer = new NewOfficer(etId.getText().toString(),
                etName.getText().toString(),
                etPhone.getText().toString(),
                etEmail.getText().toString(),
                etArea.getText().toString(),
                etAddres.getText().toString());

        showProgress();
        mDatabase.getRef().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hideProgress();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    if (data.child(etId.getText().toString()).exists()) {
                        Toast.makeText(AddOfficerActivity.this, "Officer ID already exists", Toast.LENGTH_LONG).show();
                    } else {
                        addEvent(newOfficer);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                hideProgress();
                Toast.makeText(AddOfficerActivity.this, "Error occurred please try again later", Toast.LENGTH_LONG).show();

            }
        });


    }

    private void addEvent(NewOfficer newOfficer) {
        showProgress();
        mDatabase.child("Officers").child(mDatabase.push().getKey()).setValue(newOfficer)

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
                        Toast.makeText(AddOfficerActivity.this, "Error occurred please try again later", Toast.LENGTH_LONG).show();
                    }
                });
    }

}
