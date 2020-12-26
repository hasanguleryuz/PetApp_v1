package com.example.petapp_v0;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petapp_v0.Adapter.VaccineAdapter;
import com.example.petapp_v0.Adapter.VaccineListAdapter;

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
    Button btn_showVaccineList, btn_showNow, btn_showTmorrow, btn_showWeek, btn_defineDate;
    LinearLayout footer_home, footer_calendar, footer_list;
    EditText et_vaccineDay;
    TextView tv_home, tv_list, tv_calendar;
    int vaccineDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_list);
        defineElements();
        click();
        clickFooterButtons();
        openDatePicker();
        findActivity();
    }


    private void defineElements() {
        realm = Realm.getDefaultInstance();
        pets = realm.where(Pet.class).findAll();
        lv_vaccineList = findViewById(R.id.lv_vaccineList);
        et_vaccineDay = findViewById(R.id.et_vaccineDay);
        btn_showVaccineList = findViewById(R.id.btn_showVaccineList);
        btn_showNow = findViewById(R.id.btn_showNow);
        btn_showTmorrow = findViewById(R.id.btn_showTmorrow);
        btn_showWeek = findViewById(R.id.btn_showWeek);
        footer_calendar = findViewById(R.id.footer_calendar);
        footer_home = findViewById(R.id.footer_home);
        footer_list = findViewById(R.id.footer_list);
        btn_defineDate = findViewById(R.id.btn_defineDate);
        tv_home = findViewById(R.id.tv_home);
        tv_calendar = findViewById(R.id.tv_calendar);
        tv_list = findViewById(R.id.tv_list);
    }

    private void clickFooterButtons() {
        footer_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passPetListActivity();
            }
        });
        footer_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passMainActivity();
            }
        });
    }

    private void passMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private void passPetActivity() {
        Intent intent = new Intent(this, PetsActivity.class);
        startActivity(intent);
    }

    private void passPetListActivity() {
        Intent intent = new Intent(this, PetListActivity.class);
        startActivity(intent);
    }

    private void passVaccineListActivity() {
        Intent intent = new Intent(this, VaccineListActivity.class);
        startActivity(intent);
    }

    private void click() {
        btn_showVaccineList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_vaccineDay.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Lütfen gün sayısı giriniz!!!", Toast.LENGTH_SHORT).show();
                } else {
                    vaccineDay = Integer.parseInt(String.valueOf(et_vaccineDay.getText()));
                    et_vaccineDay.getText().clear();
                    try {
                        resetButtonSource();
                        showVaccine(-1);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        btn_showNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineDay = 0;
                et_vaccineDay.getText().clear();
                try {
                    resetButtonSource();
                    desiredHoverButton(btn_showNow);
                    showVaccine(-1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        btn_showTmorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineDay = 1;
                et_vaccineDay.getText().clear();
                try {
                    resetButtonSource();
                    desiredHoverButton(btn_showTmorrow);
                    showVaccine(0);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });

        btn_showWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccineDay = 7;
                et_vaccineDay.getText().clear();
                try {
                    resetButtonSource();
                    desiredHoverButton(btn_showWeek);
                    showVaccine(-1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });
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
        if (petArrayList.size() == 0) {
            Toast.makeText(getApplicationContext(), "Aşı bulunmamaktadır...", Toast.LENGTH_SHORT).show();
        }

        VaccineListAdapter vaccineListAdapter = new VaccineListAdapter(getApplicationContext(), petArrayList);
        lv_vaccineList.setAdapter(vaccineListAdapter);
        clickListView(petArrayList);
    }

    private void showVaccineDesiredDate(final String desiredDateString) throws ParseException {
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

                            Date desiredDate = null;
                            try {
                                desiredDate = new SimpleDateFormat("dd/MM/yyyy").parse(desiredDateString);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Calendar current = Calendar.getInstance();
                            current.setTime(desiredDate);
                            current.add(Calendar.DATE, -1);
                            Date currentDate = current.getTime();
                            Calendar c = Calendar.getInstance();
                            c.setTime(desiredDate);
                            c.add(Calendar.DATE, 1);
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
        if (petArrayList.size() == 0) {
            Toast.makeText(getApplicationContext(), "Aşı bulunmamaktadır...", Toast.LENGTH_SHORT).show();
        }

        VaccineListAdapter vaccineListAdapter = new VaccineListAdapter(getApplicationContext(), petArrayList);
        lv_vaccineList.setAdapter(vaccineListAdapter);
        clickListView(petArrayList);
    }

    private void openDatePicker() {
        btn_defineDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(VaccineListActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                String desiredDate = (dayOfMonth + "/" + month + "/" + year);
                                try {
                                    showVaccineDesiredDate(desiredDate);
                                    resetButtonSource();
                                    desiredHoverButton(btn_defineDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, day);
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
            }
        });
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

    private void findActivity() {
        if (this.getClass().getSimpleName().equals("MainActivity")) {
            tv_home.setBackgroundResource(R.drawable.ic_home_hover);
        } else if (this.getClass().getSimpleName().equals("PetListActivity")) {
            tv_list.setBackgroundResource(R.drawable.ic_list_hover);
        } else if (this.getClass().getSimpleName().equals("VaccineListActivity")) {
            tv_calendar.setBackgroundResource(R.drawable.ic_calendar_hover);
        }
    }

    private void resetButtonSource() {
        btn_showWeek.setBackgroundResource(R.drawable.hover_button);
        btn_showNow.setBackgroundResource(R.drawable.hover_button);
        btn_showTmorrow.setBackgroundResource(R.drawable.hover_button);
        btn_defineDate.setBackgroundResource(R.drawable.hover_button);
    }

    private void desiredHoverButton(Button btn) {
        btn.setBackgroundResource(R.drawable.edit_button_shape_hover);
    }
}
