package com.example.petapp_v0.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.petapp_v0.Pet;
import com.example.petapp_v0.R;

import java.util.List;

public class VaccineListAdapter extends BaseAdapter {
    Context context;
    List<Pet> pets;

    public VaccineListAdapter(Context context, List<Pet> pets) {
        this.context = context;
        this.pets = pets;
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
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_vaccine_list,parent,false);
        TextView tv_ownerName = convertView.findViewById(R.id.tv_ownerName);
        TextView tv_petName = convertView.findViewById(R.id.tv_petName);
        TextView tv_petGenus = convertView.findViewById(R.id.tv_petGenus);
        ListView lv_vaccineList = convertView.findViewById(R.id.lv_vaccineList);

        tv_ownerName.setText(pets.get(position).getOwnerName());
        tv_petName.setText(pets.get(position).getPetName());
        tv_petGenus.setText(pets.get(position).getPetGenus());

       /* VaccineAdapter vaccineAdapter = new VaccineAdapter(context,pets.get(position).getPetVaccines());
        lv_vaccineList.setAdapter(vaccineAdapter);*/

        return convertView;
    }
}
