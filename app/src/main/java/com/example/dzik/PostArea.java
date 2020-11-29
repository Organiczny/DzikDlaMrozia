package com.example.dzik;

public class PostArea {
    //INPUT
    String task, uniq;
    double loc_x, loc_y;

    //OUTPUT
    String id_ad, date, descrip, response , status, loc_woj , loc_pow, loc_gm, type;
    int num_young, num_old;

    public PostArea(String task, String uniq, double loc_x, double loc_y) {
        this.task = task;
        this.uniq = uniq;
        this.loc_x = loc_x;
        this.loc_y = loc_y;
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

    public String getType() {
        return type;
    }

    public int getNum_young() {
        return num_young;
    }

    public int getNum_old() {
        return num_old;
    }
}
