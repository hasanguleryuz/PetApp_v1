package com.example.petapp_v0;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Pet extends RealmObject {

    private String petName;
    private String petGenus;
    private String petGender;
    private String petBirthyear;
    private String ownerName;
    private String ownerTelNo;
    private String ownerAddress;
    private RealmList<Vaccine> petVaccines;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerTelNo() {
        return ownerTelNo;
    }

    public void setOwnerTelNo(String ownerTelNo) {
        this.ownerTelNo = ownerTelNo;
    }

    public String getOwnerAddress() {
        return ownerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        this.ownerAddress = ownerAddress;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetGenus() {
        return petGenus;
    }

    public void setPetGenus(String petGenus) {
        this.petGenus = petGenus;
    }

    public String getPetGender() {
        return petGender;
    }

    public void setPetGender(String petGender) {
        this.petGender = petGender;
    }

    public String getPetBirthyear() {
        return petBirthyear;
    }

    public void setPetBirthyear(String petBirthyear) {
        this.petBirthyear = petBirthyear;
    }

    public RealmList<Vaccine> getPetVaccines() {
        return petVaccines;
    }

    public void setPetVaccines(RealmList<Vaccine> petVaccines) {
        this.petVaccines = petVaccines;
    }
}
