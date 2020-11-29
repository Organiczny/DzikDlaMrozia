package com.example.dzik;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.example.dzik.DataManager.SHARED_PREFS;
import static com.example.dzik.DataManager.UNIQ_ID;
import static com.example.dzik.DataManager.UNIQ_ID_DEF;

public class AcLogin extends AppCompatActivity {
    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    private static final String TAG = "FK_ACTIVITY";

    TextView tv_error;
    EditText et_email ;
    EditText et_code;

    String et_email_str;
    String et_code_str;
    int et_code_int;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_login);



        et_email = findViewById(R.id.ac_log_et_email);
        et_code = findViewById(R.id.ac_log_et_code);
        tv_error = findViewById(R.id.ac_log_tv_error);

        Button but_confirm = findViewById(R.id.ac_log_but_confirm);
        but_confirm.setOnClickListener(v -> {
            if(!checkEditText()) return;
            et_email_str = et_email.getText().toString();
            et_code_str = et_code.getText().toString();
            et_code_int = Integer.parseInt(et_code_str);
            Log.i(TAG, et_email_str + " " +et_code_str);
            request();
        });
    }



    private void request() {
        JsonPlaceHolderApi jsonPlaceHolderApi;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zefiro.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        PostLogin postLogin = new PostLogin("log", et_email_str, et_code_int);
        Call<PostLogin> call = jsonPlaceHolderApi.createPostLogin(postLogin);

        call.enqueue(new Callback<PostLogin>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PostLogin> call, Response<PostLogin> response) {
                if(!response.isSuccessful()) {
                    tv_error.setText("Code: " + response.code());
                    return;
                }
                PostLogin postLog = response.body();

                Log.i(TAG, postLog.getResponse());

                if(postLog.getResponse().equals("false")){
                    tv_error.setText("Niepoprawne dane");
                    return;
                }
                tv_error.setText("Poprawne dane: " + postLog.getResponse());

                SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(UNIQ_ID, postLog.getResponse());
                editor.apply();

                startActivity(new Intent(AcLogin.this, AcMain.class));

            }

            @Override
            public void onFailure(Call<PostLogin> call, Throwable t) {
                Log.i(TAG, "ERROR RESP");
                tv_error.setText("ERROR: " + t.getMessage());
            }
        });

    }


    private boolean checkEditText() {
        boolean check = true;

        if(et_code.getText().toString().length() < 4) {
            tv_error.setText("Pin musi mieć 4 cyfry");
            check = false;
        }
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