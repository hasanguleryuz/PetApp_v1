package com.example.petapp_v0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class VaccineAdapter extends BaseAdapter {
    Context context;
    List<Vaccine> vaccineList;

    public VaccineAdapter(Context context, List<Vaccine> vaccineList) {
        this.context = context;
        this.vaccineList = vaccineList;
    }

    @Override
    public int getCount() {
        return vaccineList.size();
    }

    @Override
    public Object getItem(int position) {
        return vaccineList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_vaccine,parent,false);
        TextView tv_vaccineType = convertView.findViewById(R.id.tv_vaccineType);
        TextView tv_vaccineDate = convertView.findViewById(R.id.tv_vaccineDate);
        TextView tv_vaccineStatus = convertView.findViewById(R.id.tv_vaccineStatus);

        tv_vaccineType.setText(vaccineList.get(position).getVaccineType());
        tv_vaccineDate.setText(vaccineList.get(position).getVaccineDate());
        tv_vaccineStatus.setText(vaccineList.get(position).getVaccineResult());

        return convertView;
    }
}
