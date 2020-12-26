package com.example.petapp_v0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petapp_v0.Adapter.VaccineAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    LinearLayout btn_call,btn_message,btn_whatsapp;
    Button btn_defineDate,btn_addVaccine;
    LinearLayout footer_home, footer_calendar, footer_list;
    int position;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet);
        defineElements();
        idGenerator();
        loadData();
        loadPetInfoToRealm();
        clickVaccineToOnListView();
        clickBtnCallAndMessage();
        openDatePicker();
        clickFooterButtons();
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
        btn_defineDate = findViewById(R.id.btn_defineDate);
        btn_whatsapp = findViewById(R.id.btn_whatsapp);
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
                if(!s_vaccineDate.equals("") && !s_vaccineType.equals("")){
                    if(isValidDate(s_vaccineDate)){
                        recordVaccineInfo();
                        et_vaccineDate.setText("");
                        et_vaccineType.setText("");
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Lütfen aşı tarihini doğru şekilde giriniz(dd/MM/yyyy).",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Lütfen bütün boşlukları tam bir şekilde doldurunuz.",Toast.LENGTH_SHORT).show();
                }
                idGenerator();
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
                vaccines.get(positionVaccine).setVaccineResult("Yapıldı");
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
                vaccines.get(positionVaccine).setVaccineResult("Yapılmadı");
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
        btn_whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "https://api.whatsapp.com/send?phone=+90 " + telno;
                try {
                    PackageManager pm = PetActivity.this.getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Whatsapp telefonunuzda yüklü değil", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
    }
    private void openDatePicker(){
        btn_defineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(PetActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                et_vaccineDate.setText(dayOfMonth + "/" + month + "/" + year);
                            }
                        }, year, month, day);
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
            }
        });
    }
    private void idGenerator(){
        if (pets.get(position).getPetVaccines().size() <= 0){
            id = 0;
        }
        else {
            id =  pets.get(position).getPetVaccines().get(pets.get(position).getPetVaccines().size()-1).getId() + 1;
        }
    }


}
