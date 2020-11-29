package com.example.dzik;

public class PostRegister {

    private String task;
    private String name;
    private String mail;
    private int code;

    private String response;

    public PostRegister(String task, String name, String mail, int code) {
        this.task = task;
        this.name = name;
        this.mail = mail;
        this.code = code;
    }

    public String getOutput() {
        return response;
    }

}
