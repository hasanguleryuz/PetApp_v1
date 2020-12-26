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

public class DailyVaccineList extends BaseAdapter {
    Context context;
    List<String> stringList;

    public DailyVaccineList(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_pet_daily,parent,false);
        TextView tv_vaccineInfo = convertView.findViewById(R.id.tv_vaccineInfo);

        tv_vaccineInfo.setText(stringList.get(position));

        return convertView;
    }
}
