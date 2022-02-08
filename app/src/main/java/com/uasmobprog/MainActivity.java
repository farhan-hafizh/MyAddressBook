package com.uasmobprog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uasmobprog.API.Api;
import com.uasmobprog.API.RetrofitClient;
import com.uasmobprog.Adapter.MainAdapter;
import com.uasmobprog.Model.Data;
import com.uasmobprog.Model.Employee;
import com.uasmobprog.Model.Employee;

import java.io.Console;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    RecyclerView view;
    MainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.employeeRecyclerView);
        view.setLayoutManager(new LinearLayoutManager(this));

        Button btnAddress = findViewById(R.id.btnAddress);
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),AddressBookActivity.class);
                startActivity(i);
            }
        });

        adapter= new MainAdapter(this);
        getData(adapter,view);
    }
    private void getData(MainAdapter adapter,RecyclerView view){
        Call<Data> call = RetrofitClient.getInstance().getMyApi().all();

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {

                Data data = response.body();
                TextView nim = findViewById(R.id.nim);
                TextView nama = findViewById(R.id.name);
                nama.setText(data.getNama());
                nim.setText(data.getNim());
                List<Employee> employeeList = data.getEmployees();
                view.setAdapter(adapter);
                adapter.setList(employeeList);
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error",t.getMessage());
            }

        });
    }
}