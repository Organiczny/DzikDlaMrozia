package com.example.dzik;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.example.dzik.DataManager.SHARED_PREFS;
import static com.example.dzik.DataManager.UNIQ_ID;
import static com.example.dzik.DataManager.UNIQ_ID_DEF;

public class AnulujDialog {

    private Activity activity;
    private AlertDialog alertDialog;
    String TAG="Mroziu";

    String reason;
    int id_ad;

    AnulujDialog(Activity myActivity){
        activity=myActivity;
    }

    void startAnulujDialog(){
        Log.d(TAG, "startBtConnectDialog: Start dialogu");
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_anuluj,null));
        builder.setCancelable(false);
        
        

        Button but_anuluj = activity.findViewById(R.id.dialog_anuluj_but_anuluj);
        
        but_anuluj.setOnClickListener(v -> {
            request();
        });
        
        alertDialog =builder.create();
        alertDialog.show();
    }

    private void request() {

        JsonPlaceHolderApi jsonPlaceHolderApi;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zefiro.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        SharedPreferences sp = activity.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        Log.i(TAG, "uniq= "+ sp.getString(UNIQ_ID, UNIQ_ID_DEF));

        PostArch postArch = new PostArch("arch", sp.getString(UNIQ_ID, UNIQ_ID_DEF), reason ,id_ad);
        Call<PostArch> call = jsonPlaceHolderApi.createPostArch(postArch);

        call.enqueue(new Callback<PostArch>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PostArch> call, Response<PostArch> response) {
                if(!response.isSuccessful()) {
                    Log.i(TAG, "Code: " + response.code());
                    Toast("Code: " + response.code(), 0);
                    return;
                }

                PostArch postArch= response.body();
                Log.i(TAG, postArch.getResponse() + "");
                if(!(postArch.getResponse() == null)){
                    if(postArch.getResponse().equals("wrong_uniq")) activity.startActivity(new Intent(activity, AcRegister.class));
                }



            }

            @Override
            public void onFailure(Call<PostArch> call, Throwable t) {
                Log.i(TAG, "ERROR RESP"+ t.getMessage());
                Toast("ERROR RESP"+ t.getMessage(), 0);
            }
        });

    }

    void dismissAnulujDialog(){
        Log.d(TAG, "dismissBtConnectDialog: Koniec dialogu");
        alertDialog.dismiss();
    }

    private void Toast(String text1, int color) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_layout));

        TextView text = layout.findViewById(R.id.tv_toast);
        text.setText(text1);
        if(color == 0) {
            layout.setBackgroundResource(R.drawable.toast_bg_red);
        }
        if(color == 1) {
            layout.setBackgroundResource(R.drawable.toast_bg_green);
        }

        Toast toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
