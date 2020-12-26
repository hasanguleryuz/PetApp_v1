package com.example.petapp_v0.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.petapp_v0.Pet;
import com.example.petapp_v0.PetActivity;
import com.example.petapp_v0.R;
import com.example.petapp_v0.UpdatePet;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;

public class PetAdapter extends BaseAdapter {

    RealmResults<Pet> petsRealm;
    Realm realm;
    Context context;
    List<Pet> pets;
    List<Pet> filterPet;
    Activity activity;

    public PetAdapter(Context context, List<Pet> pets, Activity activity) {
        realm = Realm.getDefaultInstance();
        petsRealm = realm.where(Pet.class).findAll();
        this.context = context;
        this.pets = pets;
        this.activity = activity;
        this.filterPet = new ArrayList<>();
        this.filterPet.addAll(pets);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.layout_pet, parent, false);
        TextView tv_ownerName = convertView.findViewById(R.id.tv_ownerName);
        TextView tv_petName = convertView.findViewById(R.id.tv_petName);
        TextView tv_petGenus = convertView.findViewById(R.id.tv_petGenus);

        tv_ownerName.setText(pets.get(position).getOwnerName());
        tv_petName.setText(pets.get(position).getPetName());
        tv_petGenus.setText(pets.get(position).getPetGenus());


        LinearLayout ly_layout = convertView.findViewById(R.id.ly_layout);
        Button btn_edit = convertView.findViewById(R.id.btn_edit);
        Button btn_delete = convertView.findViewById(R.id.btn_delete);

        ly_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, PetActivity.class);
                intent.putExtra("Position", (findOriginalPosition(pets.get(position).getId())));
                activity.startActivity(intent);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UpdatePet.class);
                intent.putExtra("Position", findOriginalPosition(pets.get(position).getId()));
                activity.startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogOpen(findOriginalPosition(pets.get(position).getId()));
            }
        });
        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        pets.clear();
        if (charText.length() == 0) {
            pets.addAll(filterPet);
        } else {
            for (Pet p : filterPet) {
                if (p.getOwnerName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    pets.add(p);
                }
            }
        }
        notifyDataSetChanged();
    }

    private int findOriginalPosition(long id){
        int resultposition = 0;
        for(Pet p: filterPet){
            if(p.getId() == id){
                return resultposition;
            }
            resultposition++;
        }
        return resultposition;
    }
    private void alertDialogOpen(final int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_alert, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        Button btn_yes = view.findViewById(R.id.btn_yes);
        Button btn_no = view.findViewById(R.id.btn_no);
        alert.setView(view);
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePet(position);
                dialog.cancel();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void deletePet(final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Pet pet = petsRealm.get(position);
                pet.deleteFromRealm();
            }
        });
        pets = petsRealm;
        notifyDataSetChanged();
    }

}

