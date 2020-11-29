package com.example.dzik;

public class PostArch {

    //INPUT
    String task, uniq, reason;
    int id_ad;

    //OUTPUT
    String response;

    public PostArch(String task, String uniq, String reason, int id_ad) {
        this.task = task;
        this.uniq = uniq;
        this.reason = reason;
        this.id_ad = id_ad;
    }

    public String getResponse() {
        return response;
    }
}
