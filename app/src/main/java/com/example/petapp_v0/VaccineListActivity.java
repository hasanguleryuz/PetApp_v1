package com.example.petapp_v0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class VaccineListActivity extends AppCompatActivity {
    Realm realm;
    RealmResults<Pet> pets;
    ListView lv_vaccineList;
    Button btn_showVaccineList;
    EditText et_vaccineDay;
    int vaccineDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_list);
        defineElements();
        click();
    }

    private void defineElements() {
        realm = Realm.getDefaultInstance();
        pets = realm.where(Pet.class).findAll();
        lv_vaccineList = findViewById(R.id.lv_vaccineList);
        et_vaccineDay = findViewById(R.id.et_vaccineDay);
        btn_showVaccineList = findViewById(R.id.btn_showVaccineList);
    }

    private void click() {
        btn_showVaccineList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_vaccineDay.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Litfen gün sayısı giriniz!!!",Toast.LENGTH_SHORT).show();
                    vaccineDay = -1;
                }
                else{
                    vaccineDay = Integer.parseInt(String.valueOf(et_vaccineDay.getText()));
                }
                try {
                    showVaccine();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showVaccine() throws ParseException {
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
                            Date currentDate = new Date();
                            Calendar c = Calendar.getInstance();
                            c.setTime(currentDate);
                            c.add(Calendar.DATE, vaccineDay);
                            Date currentDatePlusOne = c.getTime();

                            if (vaccineDate.getTime() < currentDatePlusOne.getTime() && vaccineDate.getTime() > currentDate.getTime()) {
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
        for (int i = 0; i < petArrayList.size(); i++) {
            if (petArrayList.get(i).getPetVaccines().size() == 0) {
                petArrayList.remove(i);
            }
        }
        if (petArrayList.size() == 0) {
            Toast.makeText(getApplicationContext(), "Aşı bulunmamaktadır...", Toast.LENGTH_SHORT).show();
        }

        VaccineListAdapter vaccineListAdapter = new VaccineListAdapter(this, petArrayList);
        lv_vaccineList.setAdapter(vaccineListAdapter);
        clickListView(petArrayList);


    }

    private void clickListView(final ArrayList<Pet> petArrayList) {
        lv_vaccineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialogOpen(petArrayList.get(position).getPetVaccines());
            }
        });
    }

    private void alertDialogOpen(RealmList<Vaccine> vaccines) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_alert_listview, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        ListView listView = view.findViewById(R.id.lv_listView);
        alert.setView(view);
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();

        VaccineAdapter vaccineAdapter = new VaccineAdapter(this, vaccines);
        listView.setAdapter(vaccineAdapter);
        dialog.show();
    }

}
