package com.example.petapp_v0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class PetActivity extends AppCompatActivity {

    Realm realm;
    RealmResults<Pet> pets;
    TextView tv_petName, tv_petGenus, tv_petGendre, tv_petBirthyear,tv_ownerName,tv_ownerTelNo,tv_ownerAddress;
    EditText et_vaccineType, et_vaccineDate;
    String s_vaccineDate, s_vaccineStatus, s_vaccineType;
    ListView lv_vaccineList;
    Button btn_addVaccine,btn_call,btn_message;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        defineElements();
        loadData();
        loadPetInfoToRealm();
        clickVaccineToOnListView();
        clickBtnCallAndMessage();
    }

    private void defineElements() {
        realm = Realm.getDefaultInstance();
        pets = realm.where(Pet.class).findAll();
        tv_petBirthyear = findViewById(R.id.tv_petBirthyear);
        tv_petGendre = findViewById(R.id.tv_petGendre);
        tv_petGenus = findViewById(R.id.tv_petGenus);
        tv_petName = findViewById(R.id.tv_petName);
        tv_ownerAddress = findViewById(R.id.tv_ownerAddress);
        tv_ownerName = findViewById(R.id.tv_ownerName);
        tv_ownerTelNo = findViewById(R.id.tv_ownerTelNo);
        et_vaccineDate = findViewById(R.id.et_vaccineDate);
        et_vaccineType = findViewById(R.id.et_vaccineType);
        //rg_vaccineStaus = findViewById(R.id.rg_vaccineStaus);
        lv_vaccineList = findViewById(R.id.lv_vaccineList);
        btn_addVaccine = findViewById(R.id.btn_addVaccine);
        btn_call = findViewById(R.id.btn_call);
        btn_message = findViewById(R.id.btn_message);
    }

    private void loadData() {
        Bundle intent = getIntent().getExtras();
        position = intent.getInt("Position");
        tv_petName.setText(pets.get(position).getPetName());
        tv_petGenus.setText(pets.get(position).getPetGenus());
        tv_petGendre.setText(pets.get(position).getPetGender());
        tv_petBirthyear.setText(pets.get(position).getPetBirthyear());
        tv_ownerTelNo.setText(pets.get(position).getOwnerTelNo());
        tv_ownerName.setText(pets.get(position).getOwnerName());
        tv_ownerAddress.setText(pets.get(position).getOwnerAddress());
    }

    private void loadPetInfoToRealm() {
        btn_addVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadVaccineInfo();
                if(isValidDate(s_vaccineDate)){
                    recordVaccineInfo();
                    et_vaccineDate.setText("");
                    et_vaccineType.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Lütfen aşı tarihini doğru şekilde giriniz(dd/MM/yyyy).",Toast.LENGTH_SHORT).show();
                }
            }
        });
        showVaccine();
    }

    private void loadVaccineInfo() {
        s_vaccineDate = et_vaccineDate.getText().toString();
        s_vaccineType = et_vaccineType.getText().toString();
        s_vaccineStatus = "Yapılmadı";
    }

    private void recordVaccineInfo() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Vaccine vaccine = realm.createObject(Vaccine.class);
                vaccine.setVaccineDate(s_vaccineDate);
                vaccine.setVaccineResult(s_vaccineStatus);
                vaccine.setVaccineType(s_vaccineType);
                Pet pet = pets.get(position);
                RealmList<Vaccine> vaccines = pet.getPetVaccines();
                vaccines.add(vaccine);
                pet.setPetVaccines(vaccines);
            }
        });
        Toast.makeText(this,"Aşı başarılı bir şeklide eklendi.",Toast.LENGTH_SHORT).show();
        showVaccine();
    }

    private void showVaccine() {
        VaccineAdapter vaccineAdapter = new VaccineAdapter(getApplicationContext(), pets.get(position).getPetVaccines());
        lv_vaccineList.setAdapter(vaccineAdapter);

    }

    private void clickVaccineToOnListView() {
        lv_vaccineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialogOpen(position);
            }
        });
    }

    private void alertDialogOpen(final int positionVaccine) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_alert_vaccine, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        Button btn_yes = view.findViewById(R.id.btn_yes);
        Button btn_no = view.findViewById(R.id.btn_no);
        Button btn_deleteVaccine = view.findViewById(R.id.btn_deleteVaccine);
        alert.setView(view);
        alert.setCancelable(true);

        final AlertDialog dialog = alert.create();
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusVaccineDone(positionVaccine);
                dialog.cancel();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatusVaccineNotDone(positionVaccine);
                dialog.cancel();
            }
        });
        btn_deleteVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteVaccine(positionVaccine);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void deleteVaccine(final int positionVaccine) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Pet pet = pets.get(position);
                RealmList<Vaccine> vaccines = pet.getPetVaccines();
                if (vaccines.size() == 1) {
                    pet.setPetVaccines(new RealmList<Vaccine>());
                } else {
                    vaccines.remove(positionVaccine);
                    pet.setPetVaccines(vaccines);
                }
            }
        });
        Toast.makeText(this,"Aşı başarılı bir şekilde kaldırıldı.",Toast.LENGTH_SHORT).show();
        showVaccine();
    }

    private void changeStatusVaccineDone(final int positionVaccine) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Pet pet = pets.get(position);
                RealmList<Vaccine> vaccines = pet.getPetVaccines();
                vaccines.get(positionVaccine).setVaccineResult(((RadioButton) (findViewById(R.id.rb_done))).getText().toString());
                pet.setPetVaccines(vaccines);
            }
        });
        showVaccine();
    }

    private void changeStatusVaccineNotDone(final int positionVaccine) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Pet pet = pets.get(position);
                RealmList<Vaccine> vaccines = pet.getPetVaccines();
                vaccines.get(positionVaccine).setVaccineResult(((RadioButton) (findViewById(R.id.rb_notDone))).getText().toString());
                pet.setPetVaccines(vaccines);
            }
        });
        showVaccine();
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

    private void clickBtnCallAndMessage(){
        final String telno = pets.get(position).getOwnerTelNo();
        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"+telno));
                startActivity(intent);
            }
        });
        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+telno));
                startActivity(intent);
            }
        });
    }
}
