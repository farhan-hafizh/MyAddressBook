package com.uasmobprog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.uasmobprog.API.RetrofitClient;
import com.uasmobprog.Model.Data;
import com.uasmobprog.Model.Employee;
import com.uasmobprog.Utils.DatabaseHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailEmployeeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static Employee emp;
    private int id;
    private static Context mContext;
    private DatabaseHandler db;
    TextView email,name, city,memberSince, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null) {
            if(bundle.containsKey("user_id")){
                id = bundle.getInt("user_id");
                Call<Data> call = RetrofitClient.getInstance().getMyApi().select(id);

                call.enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        Data data = response.body();
                        emp = data.getEmployees().get(0);
                        setContent();
                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                        Log.e("Error", t.getMessage());
                    }
                });
            }
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail_employee);
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            Log.e("No Id Found", "ID is Null");
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng place = new LatLng(Double.parseDouble(emp.getLocation().getCoordinates().getLatitude()), Double.parseDouble(emp.getLocation().getCoordinates().getLongitude()));
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(place).title("Employee Place"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
    }

    private void setContent() {

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        Button btnAddContact = (Button) findViewById(R.id.btnAddToAddressBook);
        db = new DatabaseHandler(getApplicationContext());
        db.openDatabase();
        boolean dataExist = db.isExists(emp);
        if(dataExist) btnAddContact.setVisibility(View.INVISIBLE);


        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db= new DatabaseHandler(view.getContext());
                db.openDatabase();
                db.insertEmployee(emp);
                Toast.makeText(getApplicationContext(),"Successfully Added!",Toast.LENGTH_LONG).show();
                finish();
                startActivity(getIntent());
            }
        });

        name=(TextView) findViewById(R.id.empName);
        email=(TextView)findViewById(R.id.txtEmail);
        city=(TextView)findViewById(R.id.txtCity);
        phone=(TextView)findViewById(R.id.txtPhone);
        memberSince=(TextView)findViewById(R.id.txtMemberSinced);

        name.setText(emp.getName().getFirst()+" "+emp.getName().getLast());
        email.setText("Email :"+emp.getEmail());
        city.setText("City :"+emp.getLocation().getCity()+", "+emp.getLocation().getCountry());
        phone.setText("Phone: "+emp.getPhone()+" / "+emp.getCell());
        memberSince.setText("Member since: "+emp.getRegistered().getDate());

    }
    public static Context getContext() {
        return mContext;
    }
}