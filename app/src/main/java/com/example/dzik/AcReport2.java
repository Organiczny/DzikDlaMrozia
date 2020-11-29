package com.example.dzik;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.dzik.DataManager.SHARED_PREFS;
import static com.example.dzik.DataManager.UNIQ_ID;
import static com.example.dzik.DataManager.UNIQ_ID_DEF;

public class AcReport2 extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int CAPTURE_REQUEST_CODE = 0;
    private static final int SELECT_REQUEST_CODE = 1;

    private static final String TAG = "FK_ACTIVITY";

    private String image;
    
    Button but_capture, but_select, but_send;
    ImageView imgv;
    TextView tv_error;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_report2);

        but_capture = findViewById(R.id.but_capture);
        but_select = findViewById(R.id.but_select);
        but_send = findViewById(R.id.but_send);
        imgv  = findViewById(R.id.imgv);
        tv_error = findViewById(R.id.ac_report2_tv_error);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zefiro.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        but_capture.setOnClickListener(v -> {
            if(CheckPermission()){
                Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(capture, CAPTURE_REQUEST_CODE);
            }

        });

        but_select.setOnClickListener(v -> {
            if(CheckPermission()) {
                Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(select, SELECT_REQUEST_CODE);
            }

        });

        but_send.setOnClickListener(v -> {
            
            request();
            
        });

        Button but_move1 = findViewById(R.id.but_move10);
        but_move1.setOnClickListener(v -> {
            startActivity(new Intent(AcReport2.this, AcReport.class));
        });

    }

    private void request() {

        JsonPlaceHolderApi jsonPlaceHolderApi;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zefiro.pl/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        PostAdd postAdd = new PostAdd("add", sp.getString(UNIQ_ID, UNIQ_ID_DEF), image,"description1", 1.111, 2.22, 1, 1, 2);
        Call<PostAdd> call = jsonPlaceHolderApi.createPostAdd(postAdd);

        call.enqueue(new Callback<PostAdd>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PostAdd> call, Response<PostAdd> response) {
                if(!response.isSuccessful()) {
                    tv_error.setText("Code: " + response.code());
                    return;
                }
                PostAdd postAdd = response.body();

                Log.i(TAG, postAdd.getResponse());

                if(postAdd.getResponse().equals("false")){
                    tv_error.setText("Niepoprawne dane");
                    return;
                }

                tv_error.setText("Poprawne dane: " + postAdd.getResponse());
            }

            @Override
            public void onFailure(Call<PostAdd> call, Throwable t) {
                Log.i(TAG, "ERROR RESP:  "+ t.getMessage());

                tv_error.setText("ERROR: " + t.getMessage());
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case CAPTURE_REQUEST_CODE:
            {
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imgv.setImageBitmap(bitmap);
                    generateStringImage(bitmap);
                }
            }
            break;

            case SELECT_REQUEST_CODE:
            {
                if(resultCode == RESULT_OK){

                    try {
                        Uri ImageUri = data.getData();
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), ImageUri);
                        imgv.setImageBitmap(bitmap);
                        generateStringImage(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    private void generateStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    public boolean CheckPermission() {
        if (ContextCompat.checkSelfPermission(AcReport2.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AcReport2.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(AcReport2.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AcReport2.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(AcReport2.this,
                    Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(AcReport2.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                new AlertDialog.Builder(AcReport2.this)
                        .setTitle("Permission")
                        .setMessage("Please accept the permissions")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(AcReport2.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);


                                startActivity(new Intent(AcReport2
                                        .this, AcReport2.class));
                                AcReport2.this.overridePendingTransition(0, 0);
                            }
                        })
                        .create()
                        .show();


            } else {
                ActivityCompat.requestPermissions(AcReport2.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

            return false;
        } else {

            return true;

        }
    }
}