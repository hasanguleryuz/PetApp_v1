package com.example.petapp_v0;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmResults;

public class UpdatePet extends AppCompatActivity {
    Realm realm;
    RealmResults<Pet> pets;
    EditText et_petName, et_petGenus, et_petBirthyear, et_ownerName, et_ownerAddress, et_ownerTelNo;
    RadioGroup rg_gender;
    Button btn_updatePet,btn_defineDate;
    String s_petName, s_petGenus, s_petBirthyear, s_petGender, s_ownerName, s_ownerAddress, s_ownerTelNo;
    int position;
    LinearLayout footer_home, footer_calendar, footer_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pet);
        define();
        loadDataToEditTexts();
        clickUpdateButton();
        openDatePicker();
        clickFooterButtons();
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
        btn_defineDate = findViewById(R.id.btn_defineDate);
        footer_calendar = findViewById(R.id.footer_calendar);
        footer_home = findViewById(R.id.footer_home);
        footer_list = findViewById(R.id.footer_list);
    }
    private void clickFooterButtons(){
        footer_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passPetListActivity();
            }
        });
        footer_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passVaccineListActivity();
            }
        });
        footer_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passMainActivity();
            }
        });
    }
    private void passMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void passPetListActivity(){
        Intent intent = new Intent(this, PetListActivity.class);
        startActivity(intent);
    }
    private void passVaccineListActivity(){
        Intent intent = new Intent(this,VaccineListActivity.class);
        startActivity(intent);
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

                loadPetInfo();
                if(!s_ownerName.equals("") && !s_petName.equals("") && !s_ownerAddress.equals("") && !s_petGenus.equals("") && !s_ownerTelNo.equals("") && !s_petBirthyear.equals("") && !s_petGender.equals("")){
                    if (isValidDate(s_petBirthyear)) {
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
                    } else {
                        Toast.makeText(getApplicationContext(), "Lütfen hayvanınız doğum tarihini doğru şekilde giriniz(dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Lütfen bütün boşlukları tam bir şekilde doldurunuz.",Toast.LENGTH_SHORT).show();
                }
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

    private boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    private void openDatePicker(){
        btn_defineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(UpdatePet.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                et_petBirthyear.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, year, month, day);
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
            }
        });
    }
}
