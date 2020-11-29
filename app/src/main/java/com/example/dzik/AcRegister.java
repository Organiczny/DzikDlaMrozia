package com.example.dzik;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AcRegister extends AppCompatActivity {

    private static final String TAG = "FK_ACTIVITY";

    TextView tv_error;
    EditText et_name ;
    EditText et_email ;
    EditText et_code;

    String et_name_str;
    String et_email_str;

    String et_code_str;
    int  et_code_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_register);

        tv_error = findViewById(R.id.ac_log_tv_error);
        et_name = findViewById(R.id.ac_reg_et_name);
        et_email = findViewById(R.id.ac_log_et_email);
        et_code = findViewById(R.id.ac_log_et_code);

        Button but_confirm = findViewById(R.id.ac_reg_but_confirm);
        but_confirm.setOnClickListener(v -> {
            if(!checkEditText()) return;

            et_name_str = et_name.getText().toString();
            et_email_str = et_email.getText().toString();
            et_code_str = et_code.getText().toString();

            et_code_int = Integer.parseInt(et_code_str);

            request();

        });

        Button but_haveacc = findViewById(R.id.ac_reg_but_haveacc);
        but_haveacc.setOnClickListener(v -> startActivity(new Intent(AcRegister.this , AcLogin.class)));


    }

    private void request() {
        JsonPlaceHolderApi jsonPlaceHolderApi;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zefiro.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        PostRegister postReg = new PostRegister("register", et_name_str, et_email_str, et_code_int);
        Call<PostRegister> call = jsonPlaceHolderApi.createPostRegister(postReg);

        call.enqueue(new Callback<PostRegister>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PostRegister> call, Response<PostRegister> response) {
                if(!response.isSuccessful()) {
                    tv_error.setText    ("Code: " + response.code());
                    return;
                }

                PostRegister postReg = response.body();

                if(postReg.getOutput().equals("false")) {
                    tv_error.setText("Mamy problemy z bazą urzytkowników");
                }
                else if(postReg.getOutput().toString().equals("wrong_task")) {
                    tv_error.setText("WRONG TASK");
                }
                else if(postReg.getOutput().toString().equals("mail_zajety")) {
                    tv_error.setText("Użytkownik z takim emailem już jest");
                }
                else if(postReg.getOutput().equals("true")) {
                    startActivity(new Intent(AcRegister.this , AcLogin.class));
                }
                else {
                    tv_error.setText("Nieznana odpowiedź servera: " + postReg.getOutput());
                }
            }

            @Override
            public void onFailure(Call<PostRegister> call, Throwable t) {
                Log.i(TAG, "ERROR RESP");
                tv_error.setText("ERROR: " + t.getMessage());
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private boolean checkEditText() {
        boolean check = true;
        if(et_name.getText().toString().length() < 1) {
            tv_error.setText("Niepoprawne imię");
            check = false;
        }


        if(et_code.getText().toString().length() < 4) {
            tv_error.setText("Pin musi mieć 4 cyfry");
            check = false;
        }
//        if(et_phone.getText().toString().length() < 9) tv_error.setText("Niepoprawny numer telefonu");


        if(!isEmailValid(et_email.getText().toString())) {
            tv_error.setText("Email niepoprawny");
            check = false;
        }


        return check;
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }



}