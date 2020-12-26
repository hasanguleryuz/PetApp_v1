package com.example.petapp_v0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.petapp_v0.Adapter.PetAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class PetListActivity extends AppCompatActivity {

    Realm realm;
    RealmResults<Pet> pets;
    ListView lv_petList;
    SearchView sv_search;
    List<Pet> filterPet;
    FloatingActionButton fab_add;
    LinearLayout footer_home, footer_calendar, footer_list;
    TextView tv_home, tv_list, tv_calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_list);
        defineElements();
        //clickPetToOnListView();
        showPets();
        clickAddPetBtn();
        clickFooterButtons();
        findActivity();
    }
    @Override
    protected void onResume() {
        super.onResume();
        showPets();
    }

    private void defineElements() {
        realm = Realm.getDefaultInstance();
        pets = realm.where(Pet.class).findAll();
        lv_petList = findViewById(R.id.lv_petList);
        sv_search = findViewById(R.id.sv_search);
        fab_add = findViewById(R.id.fab_add);
        footer_calendar = findViewById(R.id.footer_calendar);
        footer_home = findViewById(R.id.footer_home);
        footer_list = findViewById(R.id.footer_list);
        tv_home = findViewById(R.id.tv_home);
        tv_calendar = findViewById(R.id.tv_calendar);
        tv_list = findViewById(R.id.tv_list);
    }

    private void showPets() {
        filterPet = new ArrayList<>();
        Realm realm =  Realm.getDefaultInstance();;
        try {
            realm = Realm.getDefaultInstance();
            RealmResults<Pet> results = realm
                    .where(Pet.class)
                    .findAll();
            filterPet.addAll(realm.copyFromRealm(results));
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
        final PetAdapter petAdapter = new PetAdapter(getApplicationContext(), filterPet, this);
        lv_petList.setAdapter(petAdapter);
        sv_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                petAdapter.filter(newText);
                return false;
            }
        });

    }

    private void clickAddPetBtn(){

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passPetActivity();
            }
        });
    }

    private void clickFooterButtons(){
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

    private void passPetActivity(){
        Intent intent = new Intent(this, PetsActivity.class);
        startActivity(intent);
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
/*
    private void clickPetToOnListView() {
        lv_petList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectionAlertDialogOpen(findOriginalPosition(position));
                return true;
            }
        });
        lv_petList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PetActivity.class);
                intent.putExtra("Position", findOriginalPosition(position));
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

    private int findOriginalPosition(int position){
        int resultposition = 0;
        for(Pet p: pets){
            if(p.getId() == filterPet.get(position).getId()){
                return resultposition;
            }
            resultposition++;
        }
        return resultposition;
    }
*/

}
