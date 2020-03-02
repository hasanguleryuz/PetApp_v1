package com.example.petapp_v0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    Button btn_addPet;
    Button btn_vaccineList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineElements();
        pasActivityButtonFunc();
    }


    private void defineElements(){
        btn_addPet = findViewById(R.id.btn_addPet);
        btn_vaccineList = findViewById(R.id.btn_vaccineList);
    }

    private void pasActivityButtonFunc(){
        btn_addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passPetActivity();
            }
        });
        btn_vaccineList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passVaccineListActivity();
            }
        });
    }

    private void passPetActivity(){
        Intent intent = new Intent(this, PetsActivity.class);
        startActivity(intent);
    }
    private void passVaccineListActivity(){
        Intent intent = new Intent(this,VaccineListActivity.class);
        startActivity(intent);
    }




}
