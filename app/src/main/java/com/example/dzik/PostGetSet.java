package com.example.dzik;

public class PostGetSet {

    String task;
    String uniq;
    String name;
    String mail;
    String response;
    int code;

    public PostGetSet(String task, String uniq) {
        this.task = task;
        this.uniq = uniq;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public int getCode() {
        return code;
    }

    public String getResponse() {
        return response;
    }
}
