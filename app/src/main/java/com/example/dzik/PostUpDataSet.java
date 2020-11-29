package com.example.dzik;

public class PostUpDataSet {

    String task;
    String uniq;
    String name;
    String mail;
    int code;
    String response;

    public PostUpDataSet(String task, String uniq, String name, String mail, int code) {
        this.task = task;
        this.uniq = uniq;
        this.name = name;
        this.mail = mail;
        this.code = code;
    }

    public String getResponse() {
        return response;
    }
}
