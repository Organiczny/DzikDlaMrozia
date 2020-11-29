package com.example.dzik;

public class PostHistory {

    //INPUT
    String task, uniq;
    //OUTPUT
    String id_ad, date, descrip, response , status, loc_woj , loc_pow, loc_gm, type;
    int num_young, num_old;

    public PostHistory(String task, String uniq) {
        this.task = task;
        this.uniq = uniq;
    }

    public String getId_ad() {
        return id_ad;
    }

    public String getDate() {
        return date;
    }

    public String getDescrip() {
        return descrip;
    }

    public String getResponse() {
        return response;
    }

    public String getType() {
        return type;
    }

    public int getNum_young() {
        return num_young;
    }

    public int getNum_old() {
        return num_old;
    }

    public String getStatus() {
        return status;
    }

    public String getLoc_woj() {
        return loc_woj;
    }

    public String getLoc_pow() {
        return loc_pow;
    }

    public String getLoc_gm() {
        return loc_gm;
    }
}
