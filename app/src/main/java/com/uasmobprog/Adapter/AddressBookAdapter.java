package com.uasmobprog.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.uasmobprog.Model.Employee;
import com.uasmobprog.R;
import com.uasmobprog.Utils.DatabaseHandler;

import java.util.List;

public class AddressBookAdapter extends RecyclerView.Adapter<AddressBookAdapter.ViewHolder> {
    private List<Employee> employeeList;
    private Activity activity;
    private DatabaseHandler db;

    public AddressBookAdapter(DatabaseHandler db,Activity activity)
    {
        this.db=db;
        this.activity=activity;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.address_book_layout, parent,false);
        return new AddressBookAdapter.ViewHolder(itemView);
    }
    public int getItemCount(){
        return employeeList.size();
    }
    public void setList(List<Employee> employeeList){
        this.employeeList=employeeList;
        notifyDataSetChanged();
    }
    public void onBindViewHolder(AddressBookAdapter.ViewHolder holder, int position){
        Employee item = employeeList.get(position);
        holder.phone= item.getCell();
        holder.mail=item.getEmail();
        Picasso.get().load(item.getPicture().getLarge()).into(holder.img);
        holder.city.setText("City: "+item.getLocation().getCity()+", "+item.getLocation().getCountry());
        holder.name.setText(item.getName().getFirst()+" "+item.getName().getLast());
    }
    public Context getContext(){
        return activity;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        View view;
        String phone,mail;
        TextView name,city;
        Button email, call;
        ViewHolder(View view){
            super(view);

            img=view.findViewById(R.id.image_view);
            name=view.findViewById(R.id.txtName);
            city=view.findViewById(R.id.txtCity);
            call=view.findViewById(R.id.btnCall);
            email=view.findViewById(R.id.btnEmail);
            //calling
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    //remove -
                    phone.replaceAll("[\\s\\-()]", "");
                    callIntent.setData(Uri.parse("tel:"+phone));
                    view.getContext().startActivity(callIntent);
                }
            });
            //mailing
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto",mail, null));
                    view.getContext().startActivity(Intent.createChooser(intent, "Choose an Email client :"));
                }
            });

        }
    }
}
