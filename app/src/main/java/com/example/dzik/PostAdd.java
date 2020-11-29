package com.example.dzik;

import android.text.Editable;

public class PostAdd {

    private String task;
    private String uniq;
    private String descrip;
    private String image1;
    private String image2;
    private String image3;
    private double loc_x;
    private double loc_y;
    private int type;
    private int num_young;
    private int num_old;


    String response;

    public PostAdd(String task, String uniq, String image1, String image2, String image3, String descrip, double loc_x, double loc_y, int type, int num_young, int num_old) {
        this.task = task;
        this.uniq = uniq;
        this.descrip = descrip;
        this.loc_x = loc_x;
        this.loc_y = loc_y;
        this.type = type;
        this.num_young = num_young;
        this.num_old = num_old;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
    }

    public PostAdd(String task, String uniq, String image1, String descrip, double loc_x, double loc_y, int type, int num_young, int num_old) {
        this.task = task;
        this.uniq = uniq;
        this.descrip = descrip;
        this.loc_x = loc_x;
        this.loc_y = loc_y;
        this.type = type;
        this.num_young = num_young;
        this.num_old = num_old;
        this.image1 = image1;
    }



    public String getResponse() {
        return response;
    }
}
