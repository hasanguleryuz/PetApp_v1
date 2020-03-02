package com.example.petapp_v0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class PetsActivity extends AppCompatActivity {

    Realm realm;
    RealmResults<Pet> pets;
    EditText et_petName, et_petGenus, et_petBirthyear, et_ownerName, et_ownerAddress, et_ownerTelNo;
    RadioGroup rg_gender;
    Button btn_addPet;
    ListView lv_petList;
    String s_petName, s_petGenus, s_petBirthyear, s_petGender, s_ownerName, s_ownerAddress, s_ownerTelNo;
    RealmList<Vaccine> vaccineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);
        defineElements();
        loadPetInfoToRealm();
        clickPetToOnListView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        showPets();
    }

    private void defineElements() {
        realm = Realm.getDefaultInstance();
        pets = realm.where(Pet.class).findAll();
        et_petName = findViewById(R.id.et_petName);
        et_petGenus = findViewById(R.id.et_petGenus);
        et_petBirthyear = findViewById(R.id.et_petBirthyear);
        et_ownerAddress = findViewById(R.id.et_ownerAddress);
        et_ownerName = findViewById(R.id.et_ownerName);
        et_ownerTelNo = findViewById(R.id.et_ownerTelNo);
        rg_gender = findViewById(R.id.rg_gender);
        btn_addPet = findViewById(R.id.btn_addPet);
        lv_petList = findViewById(R.id.lv_petList);
    }

    private void loadPetInfoToRealm() {
        btn_addPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPetInfo();
                if (isValidDate(s_petBirthyear)) {
                    recordPetInfo();
                    et_petBirthyear.setText("");
                    et_petGenus.setText("");
                    et_petName.setText("");
                    et_ownerAddress.setText("");
                    et_ownerName.setText("");
                    et_ownerTelNo.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Lütfen hayvanınız doğum tarihini doğru şekilde giriniz(dd/MM/yyyy)", Toast.LENGTH_SHORT).show();
                }
            }
        });
        showPets();

    }


    private void loadPetInfo() {
        s_ownerAddress = et_ownerAddress.getText().toString();
        s_ownerName = et_ownerName.getText().toString();
        s_ownerTelNo = et_ownerTelNo.getText().toString();
        s_petName = et_petName.getText().toString();
        s_petGenus = et_petGenus.getText().toString();
        s_petBirthyear = et_petBirthyear.getText().toString();
        s_petGender = ((RadioButton) findViewById(rg_gender.getCheckedRadioButtonId())).getText().toString();
        vaccineList = new RealmList<>();
    }

    private void recordPetInfo() {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Pet pet = realm.createObject(Pet.class);
                pet.setOwnerAddress(s_ownerAddress);
                pet.setOwnerName(s_ownerName);
                pet.setOwnerTelNo(s_ownerTelNo);
                pet.setPetName(s_petName);
                pet.setPetGender(s_petGender);
                pet.setPetBirthyear(s_petBirthyear);
                pet.setPetGenus(s_petGenus);
                pet.setPetVaccines(vaccineList);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(), "Hayvanınız başarılı bir şekilde eklendi", Toast.LENGTH_SHORT).show();
                showPets();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(getApplicationContext(), "Hayvanınız bir hatadan dolayı eklenemedi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPets() {
        PetAdapter petAdapter = new PetAdapter(getApplicationContext(), pets, this);
        lv_petList.setAdapter(petAdapter);

    }

    private void clickPetToOnListView() {
        lv_petList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectionAlertDialogOpen(position);
                return true;
            }
        });
        lv_petList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PetActivity.class);
                intent.putExtra("Position", position);
                startActivity(intent);
            }
        });

    }

    private void selectionAlertDialogOpen(final int position) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_long_click_selection, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_update = view.findViewById(R.id.btn_update);
        alert.setView(view);
        alert.setCancelable(true);
        final AlertDialog dialog = alert.create();
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogOpen(position);
                dialog.cancel();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UpdatePet.class);
                intent.putExtra("Position", position);
                startActivity(intent);
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void alertDialogOpen(final int position) {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_alert, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        Button btn_yes = view.findViewById(R.id.btn_yes);
        Button btn_no = view.findViewById(R.id.btn_no);
        alert.setView(view);
        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePet(position);
                dialog.cancel();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void deletePet(final int position) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Pet pet = pets.get(position);
                pet.deleteFromRealm();
                showPets();
            }
        });
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

}
