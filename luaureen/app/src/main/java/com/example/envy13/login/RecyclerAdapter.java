package com.example.envy13.login;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Vanessa on 15/03/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {


    List<UserProduct> list;
    Context context;
    String img;

    String keyPhone =  "phone";

    public RecyclerAdapter(List<UserProduct> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        MyHolder myHolder = new MyHolder(view);

        return myHolder;
    }

    public Bitmap StringToBitMap(String encodedString){
        img = encodedString;
        try {
            byte [] encodeByte=android.util.Base64.decode(encodedString,android.util.Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerAdapter.MyHolder holder, int position) {
        UserProduct myList = list.get(position);
        holder.email.setText(myList.getEmail());
        holder.contact.setText(myList.getContact());
        holder.location.setText(myList.getLocation());
        holder.productName.setText(myList.getProductName());
        holder.productAmount.setText(myList.getProductAmount());
        holder.productDescription.setText(myList.getProductDescription());

//        glide library for loading image
        Glide.with(context).load(myList.getprodImage()).into(holder.prodImg);

        holder.contactfarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(keyPhone, holder.contact.getText().toString());
                contactAlertDialog(bundle);
            }
        });

    }

    private void contactAlertDialog(Bundle bundle) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Send a text to this farmer for more information");
        builder.setCancelable(true);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.contact_farmer_popup, null);
        builder.setView(view);

        final TextView phonec = view.findViewById(R.id.contactno);
        final EditText yourtx = view.findViewById(R.id.yourText);

//        passes the contact number from the recycler-cardview to the alertdialog/popup
        phonec.setText(bundle.getString(keyPhone));

        builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String phonenumber = phonec.getText().toString().trim();
                String textmessage = yourtx.getText().toString().trim();
                if (textmessage.isEmpty()) {
                    Toast.makeText(RecyclerAdapter.this.context, "Kindly fill in the text field", Toast.LENGTH_SHORT).show();
                } else {
                    textFarmer(phonenumber, textmessage);
                }

            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }

    private void textFarmer(String phonenumber, String textmessage) {

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, new Intent(context, RecyclerAdapter.class), 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phonenumber, null, textmessage, pendingIntent, null);
    }

    @Override
    public int getItemCount() {

        int arr = 0;

        try {
            if (list.size() == 0) {
                arr = 0;
            }else {
                arr = list.size();
            }
        }catch (Exception ignored) { }

        return arr;

    }

    public class MyHolder extends RecyclerView.ViewHolder {

        TextView email, contact, location, productName, productAmount, productDescription;
        ImageView prodImg;
        Button contactfarmer;


        TextView phone;
        EditText yourText;

        public MyHolder(View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.cvemail);
            contact = itemView.findViewById(R.id.cvcontact);
            location = itemView.findViewById(R.id.cvproductlocation);
            productName = itemView.findViewById(R.id.cvproductname);
            productAmount = itemView.findViewById(R.id.cvproductamount);
            productDescription = itemView.findViewById(R.id.cvproductdescription);
            prodImg = itemView.findViewById(R.id.thumbnail);
            contactfarmer = itemView.findViewById(R.id.callfarmer);

            phone = itemView.findViewById(R.id.contactno);
            yourText = itemView.findViewById(R.id.yourText);
        }

    }

}
