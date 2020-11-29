package com.example.dzik;

public class PostLogin {

    private String task;
    private String mail;
    private int code;

    private String response;

    public PostLogin(String task, String mail, int code) {
        this.task = task;
        this.mail = mail;
        this.code = code;
    }

    public String getResponse() {
        return response;
    }


}
