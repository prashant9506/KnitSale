package com.example.knitsale.model;

import android.net.Uri;

import java.net.URL;

public class ProfileData {
    String name , branchyear , hostelroom ,mob , alternativemob , profileurl , email ;

    public ProfileData(String email, String name , String mob , String alternativemob , String branchyear , String hostelroom , String profileurl){
        this.email = email;
        this.name =name;
        this.mob = mob;
        this.alternativemob = alternativemob;
        this.branchyear = branchyear;
        this.hostelroom = hostelroom;
        this.profileurl = profileurl;
    }

    public ProfileData(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBranchyear() {
        return branchyear;
    }

    public void setBranchyear(String branchyear) {
        this.branchyear = branchyear;
    }

    public String getHostelroom() {
        return hostelroom;
    }

    public void setHostelroom(String hostelroom) {
        this.hostelroom = hostelroom;
    }

    public String getMob() {
        return mob;
    }

    public void setMob(String mob) {
        this.mob = mob;
    }

    public String getAlternativemob() {
        return alternativemob;
    }

    public void setAlternativemob(String alternativemob) {
        this.alternativemob = alternativemob;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }
}
