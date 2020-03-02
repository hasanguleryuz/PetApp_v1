package com.example.petapp_v0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;

public class UpdatePet extends AppCompatActivity {
    Realm realm;
    RealmResults<Pet> pets;
    EditText et_petName, et_petGenus, et_petBirthyear, et_ownerName, et_ownerAddress, et_ownerTelNo;
    RadioGroup rg_gender;
    Button btn_updatePet;
    String s_petName, s_petGenus, s_petBirthyear, s_petGender, s_ownerName, s_ownerAddress, s_ownerTelNo;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet);
        define();
        loadDataToEditTexts();
        clickUpdateButton();
    }

    private void define() {
        realm = Realm.getDefaultInstance();
        pets = realm.where(Pet.class).findAll();
        et_petName = findViewById(R.id.et_petName);
        et_petGenus = findViewById(R.id.et_petGenus);
        et_petBirthyear = findViewById(R.id.et_petBirthyear);
        et_ownerAddress = findViewById(R.id.et_ownerAddress);
        et_ownerName = findViewById(R.id.et_ownerName);
        et_ownerTelNo = findViewById(R.id.et_ownerTelNo);
        rg_gender = findViewById(R.id.rg_gender);
        btn_updatePet = findViewById(R.id.btn_updatePet);
    }

    private void loadDataToEditTexts() {
        Bundle intent = getIntent().getExtras();
        position = intent.getInt("Position");
        et_petName.setText(pets.get(position).getPetName());
        et_petGenus.setText(pets.get(position).getPetGenus());
        if (pets.get(position).getPetGender().equals("Erkek")) {
            ((RadioButton) (rg_gender.getChildAt(0))).setChecked(true);
        } else {
            ((RadioButton) (rg_gender.getChildAt(1))).setChecked(true);
        }
        et_petBirthyear.setText(pets.get(position).getPetBirthyear());
        et_ownerAddress.setText(pets.get(position).getOwnerAddress());
        et_ownerName.setText(pets.get(position).getOwnerName());
        et_ownerTelNo.setText(pets.get(position).getOwnerTelNo());
    }

    private void clickUpdateButton() {
        btn_updatePet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Pet pet = pets.get(position);
                loadPetInfo();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        pet.setPetName(s_petName);
                        pet.setPetGenus(s_petGenus);
                        pet.setPetGender(s_petGender);
                        pet.setPetBirthyear(s_petBirthyear);
                        pet.setOwnerAddress(s_ownerAddress);
                        pet.setOwnerName(s_ownerName);
                        pet.setOwnerTelNo(s_ownerTelNo);
                    }
                });
                Toast.makeText(getApplicationContext(),"Hayvan başarılı bir şekilde güncellendi.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void loadPetInfo() {
        s_ownerAddress = et_ownerAddress.getText().toString();
        s_ownerName = et_ownerName.getText().toString();
        s_ownerTelNo = et_ownerTelNo.getText().toString();
        s_petName = et_petName.getText().toString();
        s_petGenus = et_petGenus.getText().toString();
        s_petBirthyear = et_petBirthyear.getText().toString();
        s_petGender = ((RadioButton) findViewById(rg_gender.getCheckedRadioButtonId())).getText().toString();

    }


}
