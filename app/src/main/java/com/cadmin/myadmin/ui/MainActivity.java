package com.cadmin.myadmin.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cadmin.myadmin.BaseActivity;
import com.cadmin.myadmin.R;
import com.cadmin.myadmin.models.ComplaintModel;
import com.cadmin.myadmin.models.CustomAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        CustomAdapter.IItemClickListener, View.OnClickListener, TextWatcher {
    public static final String mypreference = "mypref";
    public static final String isAdmin = "nameKey";
    public CustomAdapter adapter;
    private List<ComplaintModel> complaintsList = new ArrayList<>();
    private List<ComplaintModel> complaintsSearchList = new ArrayList<>();
    private DatabaseReference mDatabase;
    private RecyclerView mRvComplaints;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sharedpreferences;
    private DrawerLayout drawer;
    private boolean isAdminLogin;
    private EditText etSearch;
    private TextView mNewComplaint,mTvViewComplainy,mTvUpdateComplaint,mTvLogout,mTvAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mRvComplaints = (RecyclerView) findViewById(R.id.rv_complaints);
        etSearch = (EditText)findViewById(R.id.etSearch);
        mTvUpdateComplaint = (TextView) findViewById(R.id.tvUpdate);
        mTvAdd = (TextView) findViewById(R.id.tvAddOficer);
        mTvLogout = (TextView) findViewById(R.id.tvLogout);
        mTvViewComplainy = (TextView) findViewById(R.id.tvView);
        mNewComplaint = (TextView) findViewById(R.id.tvNewComplaint);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mNewComplaint.setOnClickListener(this);
        mTvUpdateComplaint.setOnClickListener(this);
        mTvAdd.setOnClickListener(this);
        mTvLogout.setOnClickListener(this);
        mTvViewComplainy.setOnClickListener(this);
        etSearch.addTextChangedListener(this);

        layoutManager = new LinearLayoutManager(this);
        mRvComplaints.setLayoutManager(layoutManager);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //printHashKey(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        adapter = new CustomAdapter(complaintsList, this, this);
        mRvComplaints.setAdapter(adapter);
        Intent intent = getIntent();
        putPrefes(intent);
        checkLoginType();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllComplaints();
    }

    private void checkLoginType() {
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains("IS_ADMIN")) {
            isAdminLogin = (sharedpreferences.getBoolean("IS_ADMIN", false));
        }

        if(!isAdminLogin){
            mTvUpdateComplaint.setVisibility(View.GONE);
            mTvAdd.setVisibility(View.GONE);
        }else{
            mTvUpdateComplaint.setVisibility(View.GONE);
            mTvAdd.setVisibility(View.VISIBLE);
        }
    }

    private void putPrefes(Intent intent) {
        boolean isAdmin;
        if(intent!=null && intent.hasExtra("IS_ADMIN")){
            isAdmin = intent.getBooleanExtra("IS_ADMIN",false);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean("IS_ADMIN", isAdmin);
            editor.apply();
        }


    }

    private void getAllComplaints() {
        complaintsList = new ArrayList<>();
        showProgress();
        mDatabase.child("Complaints").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                hideProgress();
                complaintsList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ComplaintModel complaintModel = postSnapshot.getValue(ComplaintModel.class);
                    complaintsList.add(complaintModel);

                    // here you can access to name property like university.name

                }
                adapter.updateList(complaintsList);
                mRvComplaints.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideProgress();
                Toast.makeText(MainActivity.this, "The read failed: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new) {
            startActivity(new Intent(this, NewComplaintActivity.class));
        } else if (id == R.id.nav_update) {
            startActivity(new Intent(this, UpdateComplaint.class));

        } else if (id == R.id.nav_view) {
            startActivity(new Intent(this, ViewComplaintActivity.class));

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            clearStorage();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clearStorage() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove("IS_ADMIN"); // will delete key name
        editor.commit(); // commit changes
        editor.clear();
        editor.commit(); // c
    }

    @Override
    public void onItemClick(ComplaintModel complaintModel) {
        Intent intent = (new Intent(this,ViewComplaintActivity.class));
        intent.putExtra("Complaint",complaintModel);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNewComplaint:{
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, NewComplaintActivity.class));
                break;
            }
            case R.id.tvAddOficer:{
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, AddOfficerActivity.class));
                break;
            }
            case R.id.tvLogout:{
                drawer.closeDrawer(GravityCompat.START);
                FirebaseAuth.getInstance().signOut();
                clearStorage();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
            }
            case R.id.tvView:{
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, ViewComplaintActivity.class));
                break;

            }
            case R.id.tvUpdate:{
                drawer.closeDrawer(GravityCompat.START);
                startActivity(new Intent(this, UpdateComplaint.class));
                break;

            }

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        filterList(s.toString());
    }

    private void filterList(String search) {
        complaintsSearchList.clear();
        for(int i=0;i<complaintsList.size();i++){
            if(complaintsList.get(i).complaint.toLowerCase().contains(search.toLowerCase())){
                complaintsSearchList.add(complaintsList.get(i));
            }
        }

        adapter.updateList(complaintsSearchList);
        adapter.notifyDataSetChanged();

        /*if(complaintsSearchList.size()>0) {
            adapter.updateList(complaintsSearchList);
            adapter.notifyDataSetChanged();
        }else if(search.length()==0){
            adapter.updateList(complaintsList);
            adapter.notifyDataSetChanged();
        }*/
    }
}
