package com.example.petapp_v0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class PetAdapter extends BaseAdapter {

    Context context;
    List<Pet> pets;
    Activity activity;

    public PetAdapter(Context context, List<Pet> pets, Activity activity) {
        this.context = context;
        this.pets = pets;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pets.size();
    }

    @Override
    public Object getItem(int position) {
        return pets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.layout_pet,parent,false);
        TextView tv_ownerName = convertView.findViewById(R.id.tv_ownerName);
        TextView tv_ownerTelNo = convertView.findViewById(R.id.tv_ownerTelNo);
        TextView tv_ownerAddress = convertView.findViewById(R.id.tv_ownerAddress);
        TextView tv_petName = convertView.findViewById(R.id.tv_petName);
        TextView tv_petGenus = convertView.findViewById(R.id.tv_petGenus);
        TextView tv_petGendre = convertView.findViewById(R.id.tv_petGendre);
        TextView tv_petBirthyear = convertView.findViewById(R.id.tv_petBirthyear);

        tv_ownerAddress.setText(pets.get(position).getOwnerAddress());
        tv_ownerName.setText(pets.get(position).getOwnerName());
        tv_ownerTelNo.setText(pets.get(position).getOwnerTelNo());
        tv_petName.setText(pets.get(position).getPetName());
        tv_petGenus.setText(pets.get(position).getPetGenus());
        tv_petGendre.setText(pets.get(position).getPetGender());
        tv_petBirthyear.setText(pets.get(position).getPetBirthyear());

        return convertView;
    }
}
