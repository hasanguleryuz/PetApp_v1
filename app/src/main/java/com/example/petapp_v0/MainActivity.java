package com.example.petapp_v0;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petapp_v0.Adapter.DailyVaccineList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    Realm realm;
    RealmResults<Pet> pets;
    List<String> stringList;
    Button btn_addPet;
    Button btn_vaccineList;
    Button btn_petList;
    LinearLayout footer_home, footer_calendar, footer_list;
    TextView tv_home, tv_list, tv_calendar, tv_date;
    ListView lv_nowVaccineList, lv_tmorrowVaccineList;
    int vaccineDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defineElements();
        pasActivityButtonFunc();
        clickFooterButtons();
        findActivity();
        findDate();
        vaccineDay = 0;
        try {
            showVaccine(-1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void defineElements(){
        realm = Realm.getDefaultInstance();
        pets = realm.where(Pet.class).findAll();
        btn_addPet = findViewById(R.id.btn_addPet);
        footer_calendar = findViewById(R.id.footer_calendar);
        footer_home = findViewById(R.id.footer_home);
        footer_list = findViewById(R.id.footer_list);
        tv_home = findViewById(R.id.tv_home);
        tv_calendar = findViewById(R.id.tv_calendar);
        tv_list = findViewById(R.id.tv_list);
        lv_nowVaccineList = findViewById(R.id.lv_nowVaccineList);
        lv_tmorrowVaccineList = findViewById(R.id.lv_nowVaccineList);
        tv_date = findViewById(R.id.tv_date);
        stringList = new ArrayList<>();
        btn_petList = findViewById(R.id.btn_petList);
        btn_vaccineList = findViewById(R.id.btn_vaccineList);
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
    }

    private void passMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void pasActivityButtonFunc(){
        btn_addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passPetActivity();
            }
        });

        btn_petList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passPetListActivity();
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
    private void passPetListActivity(){
        Intent intent = new Intent(this, PetListActivity.class);
        startActivity(intent);
    }
    private void passVaccineListActivity(){
        Intent intent = new Intent(this,VaccineListActivity.class);
        startActivity(intent);
    }

    private void findActivity(){
        if(this.getClass().getSimpleName().equals("MainActivity")){
            tv_home.setBackgroundResource(R.drawable.ic_home_hover);
        }
        else if (this.getClass().getSimpleName().equals("PetListActivity")){
            tv_list.setBackgroundResource(R.drawable.ic_list_hover);
        }
        else if (this.getClass().getSimpleName().equals("VaccineListActivity")){
            tv_calendar.setBackgroundResource(R.drawable.ic_calendar_hover);
        }
    }

    private void showVaccine(final int limit) throws ParseException {
        ArrayList<Pet> petArrayList = new ArrayList<>();
        final ArrayList<Pet> finalPetArrayList = petArrayList;
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                for (Pet p : pets) {
                    Pet tempPet = new Pet();
                    tempPet.setOwnerTelNo(p.getOwnerTelNo());
                    tempPet.setOwnerName(p.getOwnerName());
                    tempPet.setOwnerAddress(p.getOwnerAddress());
                    tempPet.setPetName(p.getPetName());
                    tempPet.setPetGenus(p.getPetGenus());
                    tempPet.setPetBirthyear(p.getPetBirthyear());
                    tempPet.setPetGender(p.getPetGender());
                    if (p.getPetVaccines().size() > 0) {
                        RealmList<Vaccine> vaccineRealmList = new RealmList<>();
                        RealmList<Vaccine> vaccines = p.getPetVaccines();
                        for (Vaccine v : vaccines) {
                            Date vaccineDate = null;
                            try {
                                vaccineDate = new SimpleDateFormat("dd/MM/yyyy").parse(v.getVaccineDate());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Date currentDateOriginal = new Date();
                            Calendar current = Calendar.getInstance();
                            current.setTime(currentDateOriginal);
                            current.add(Calendar.DATE, limit);
                            Date currentDate = current.getTime();
                            Calendar c = Calendar.getInstance();
                            c.setTime(currentDateOriginal);
                            c.add(Calendar.DATE, vaccineDay);
                            Date currentDatePlusOne = c.getTime();

                            if (vaccineDate.getTime() < currentDatePlusOne.getTime() && vaccineDate.getTime() > currentDate.getTime() && v.getVaccineResult().equals("Yapılmadı")) {
                                vaccineRealmList.add(v);
                            }
                        }
                        tempPet.setPetVaccines(vaccineRealmList);
                        finalPetArrayList.add(tempPet);
                    }
                }
            }
        });
        petArrayList = finalPetArrayList;
        for (int i = (petArrayList.size() - 1); i >= 0; i--) {
            if (petArrayList.get(i).getPetVaccines().size() == 0) {
                petArrayList.remove(i);
            }
        }
        for(Pet p: petArrayList){
            for(Vaccine v:p.getPetVaccines()){
                stringList.add(p.getOwnerName()+" isimli müşterinin "+"'"+p.getPetName()+"'"+" evcil hayvanının "+"'"+v.getVaccineType()+"'"+" aşısı vardır.");
            }
        }

        DailyVaccineList dailyVaccineList = new DailyVaccineList(getApplicationContext(), stringList);
        lv_nowVaccineList.setAdapter(dailyVaccineList);
    }

    private void findDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("    EEEE, dd/MMM/yyyy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        tv_date.setText(currentDateandTime);
    }


}
