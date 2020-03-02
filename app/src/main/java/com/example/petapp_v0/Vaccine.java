package com.example.petapp_v0;

import io.realm.RealmObject;

public class Vaccine extends RealmObject{
    private String vaccineType;
    private String vaccineDate;
    private String vaccineResult;

    public String getVaccineType() {
        return vaccineType;
    }

    public void setVaccineType(String vaccineType) {
        this.vaccineType = vaccineType;
    }

    public String getVaccineDate() {
        return vaccineDate;
    }

    public void setVaccineDate(String vaccineDate) {
        this.vaccineDate = vaccineDate;
    }

    public String getVaccineResult() {
        return vaccineResult;
    }

    public void setVaccineResult(String vaccineResult) {
        this.vaccineResult = vaccineResult;
    }
}
