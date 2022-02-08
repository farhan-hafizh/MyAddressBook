package com.uasmobprog.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.uasmobprog.DetailEmployeeActivity;
import com.uasmobprog.Model.Employee;
import com.uasmobprog.R;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private List<Employee> employeeList;
    private Activity activity;

    public MainAdapter(Activity activity)
    {
        this.activity=activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employees_layout, parent,false);
        return new ViewHolder(itemView);
    };
    public int getItemCount(){
        return employeeList.size();
    }
    private boolean checkStatus(int n){
        return n!=0;
    }
    public void setList(List<Employee> employeeList){
        this.employeeList=employeeList;
        notifyDataSetChanged();
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        Employee item = employeeList.get(position);
        holder.id = item.getEmployeeId();
        Picasso.get().load(item.getPicture().getLarge()).into(holder.img);
        holder.city.setText("City: "+item.getLocation().getCity()+", "+item.getLocation().getCountry());
        holder.phone.setText("Phone: "+item.getPhone()+" / "+item.getCell());
        holder.memberSinced.setText("Member since: "+item.getRegistered().getDate());
        holder.name.setText(item.getName().getFirst()+" "+item.getName().getLast());
    }
    public Context getContext(){
        return activity;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        View view;
        int id;
        TextView name,city,phone,memberSinced;
        ViewHolder(View view){
            super(view);
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), DetailEmployeeActivity.class);
//                    Intent i = new Intent(v.getContext(), MapsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("user_id",id);
                    i.putExtras(bundle);
                    v.getContext().startActivity(i);
                }
            });
            img=view.findViewById(R.id.image_view);
            name=view.findViewById(R.id.txtName);
            city=view.findViewById(R.id.txtCity);
            phone=view.findViewById(R.id.txtPhone);
            memberSinced=view.findViewById(R.id.txtMemberSinced);
        }
    }
}
