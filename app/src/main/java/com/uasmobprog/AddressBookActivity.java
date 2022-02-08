package com.uasmobprog;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uasmobprog.API.RetrofitClient;
import com.uasmobprog.Adapter.AddressBookAdapter;
import com.uasmobprog.Adapter.MainAdapter;
import com.uasmobprog.Model.Data;
import com.uasmobprog.Model.Employee;
import com.uasmobprog.Utils.DatabaseHandler;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressBookActivity extends AppCompatActivity {
    RecyclerView view;
    AddressBookAdapter adapter;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        view = findViewById(R.id.employeeRecyclerView);
        view.setLayoutManager(new LinearLayoutManager(this));

        db= new DatabaseHandler(this);
        db.openDatabase();
        List<Employee> empList = db.getAllEmployee();
        Collections.reverse(empList);

        adapter= new AddressBookAdapter(db,this);
        adapter.setList(empList);
        view.setAdapter(adapter);
//        getData(adapter,view);
    }
//    private void getData(AddressBookAdapter adapter,RecyclerView view){
//
//    }
}