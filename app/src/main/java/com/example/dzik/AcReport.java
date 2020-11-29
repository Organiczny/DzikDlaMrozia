package com.example.dzik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.dzik.DataManager.SHARED_PREFS;
import static com.example.dzik.DataManager.UNIQ_ID;
import static com.example.dzik.DataManager.UNIQ_ID_DEF;

public class AcReport extends AppCompatActivity {
    private static final String TAG = "FK_ACTIVITY";
    private static final int PICK_IMAGE = 100;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private ImageView imageView1Photo;
    private ImageView imageView2Photo;
    private ImageView imageView3Photo;
    EditText editTextAdult;
    EditText editTextChlid;
    int nrZdj;
    Uri[] tabUri = new Uri[3];
    Bitmap [] tabBitmap = new Bitmap[3];

    FusedLocationProviderClient fusedLocationProviderClient;

    RadioGroup radioGroupLiveDead;
    RadioButton radioButtonLive;
    RadioButton radioButtonDead;
    Dialog mDialog;
    ImageView imageViewFromGallery;
    ImageView imageViewFromCamera;
    EditText et_opis;

    int typeToReq, youngToReq, oldToReq;
    String image1, image2, image3;
    double lat1 = 0.1;
    double lon1 = 0.1;

    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2 ;
    RadioButton radioButton3 ;
    RadioButton radioButton4 ;
    RadioButton radioButton5;
    RadioButton radioButton6 ;
    EditText editText ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_report);
        Button btnZglos=findViewById(R.id.btn_report);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkLocationPerms();

        tabUri[0] = null;
        tabUri[1] = null;
        tabUri[2] = null;

        tabBitmap[0] = null;
        tabBitmap[1] = null;
        tabBitmap[2] = null;

        radioGroupLiveDead=(RadioGroup) findViewById(R.id.live_dead_button_view) ;
        radioButtonLive = (RadioButton) findViewById(R.id.radio_live);
        radioButtonDead = (RadioButton) findViewById(R.id.radio_dead);
        imageView1 = (ImageView) findViewById(R.id.image_view1);
        imageView2 = (ImageView) findViewById(R.id.image_view2);
        imageView3 = (ImageView) findViewById(R.id.image_view3);
        editTextAdult=(EditText) findViewById(R.id.et_adult);
        editTextChlid=(EditText) findViewById(R.id.et_child);
        et_opis = findViewById(R.id.et_opis);

        //editTextAdult.setText(0);
        //editTextChlid.setText(0);
/*        NumberPicker numberPickerAdult= findViewById(R.id.np_adult);
        numberPickerAdult.setMaxValue(0);
        numberPickerAdult.setMaxValue(99);
        numberPickerAdult.get*/
/*        radioGroup = (RadioGroup)findViewById(R.id.adult_button_view);
        radioGroup.clearCheck();*/

        btnZglos.setOnClickListener(v -> {
            switch (sprawdzPoprawnosc()) {
                case 0:
                    Log.i(TAG, "opprawnie");
                    generateValues();
                    sendReqest();
                    break;
                case 1:
                    Log.i(TAG, "zla liczba dzikow");
                    Toast("Niepoprwna liczba dzików", 0);
                    break;
                case 2:
                    Log.i(TAG, "brak zdj padliny");
                    Toast("Nie zamieszczono zjęcią", 0);
                    break;
                default:
                    Log.i(TAG, "defalut");
                    break;
            }

        });
        imageView1.setOnClickListener(v -> {
            nrZdj=0;
            Log.d(TAG, "onClick: asekuracyjnie");
            pokazDialogPhoto();

        });
        imageView2.setOnClickListener(v -> {
            nrZdj=1;
            pokazDialogPhoto();
        });
        imageView3.setOnClickListener(v -> {
            nrZdj=2;
            pokazDialogPhoto();
        });

    }



    private void sendReqest() {

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

        Log.i(TAG,  " " + lat1 + lon1);

        PostAdd postAdd = new PostAdd("add", sp.getString(UNIQ_ID, UNIQ_ID_DEF), image1, image2, image3, et_opis.getText().toString(), lat1, lon1, typeToReq, youngToReq, oldToReq);
        Call<PostAdd> call = jsonPlaceHolderApi.createPostAdd(postAdd);

        call.enqueue(new Callback<PostAdd>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PostAdd> call, Response<PostAdd> response) {
                if(!response.isSuccessful()) {
//                    tv_error.setText("Code: " + response.code());

                    return;
                }
                PostAdd postAdd = response.body();
                Log.i(TAG, postAdd.getResponse());
                if(!(postAdd.getResponse() == null)) {
                    if( postAdd.getResponse().equals("wrong_uniq")) startActivity(new Intent(AcReport.this, AcRegister.class));
                }


                if(postAdd.getResponse().equals("false")){
                    Toast("Niepoprawne dane", 0);
                    return;
                }

                Toast("Wysłano poprawnie dane", 1);
//
                Log.i(TAG, "poprawne dane");
            }

            @Override
            public void onFailure(Call<PostAdd> call, Throwable t) {
                Log.i(TAG, "ERROR RESP:  "+ t.getMessage());

               Toast("Nie połączono z serverem", 1);
            }
        });

    }

    private void Toast(String text1, int color) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_layout));

        TextView text = layout.findViewById(R.id.tv_toast);
        text.setText(text1);
        if(color == 0) {
            layout.setBackgroundResource(R.drawable.toast_bg_red);
        }
        if(color == 1) {
            layout.setBackgroundResource(R.drawable.toast_bg_green);
        }

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void generateValues() {
//        checkLocationPerms();

        youngToReq = Integer.parseInt(editTextChlid.getText().toString());
        oldToReq =Integer.parseInt(editTextAdult.getText().toString());

        generateStringsBitmaps();
    }

    private String generateStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
        return image;
    }

    private void generateStringsBitmaps() {
        image1 = "null";
        image2 = "null";
        image3 = "null";

        Vector<Bitmap> listBitmap = new Vector<Bitmap>() ;

        if(tabUri[0] != null) {

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tabUri[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            listBitmap.addElement(bitmap);
        }
        if(tabUri[1] != null) {
            Log.i(TAG, "uri 1 null");
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tabUri[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            listBitmap.addElement(bitmap);
        }
        if(tabUri[2] != null) {
            Log.i(TAG, "uri 2  null");
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), tabUri[2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            listBitmap.addElement(bitmap);
        }

        if(tabBitmap[0] != null) {

            listBitmap.addElement(tabBitmap[0]);
        }
        if(tabBitmap[1] != null) {

            listBitmap.addElement(tabBitmap[1]);
        }
        if(tabBitmap[2] != null) {

            listBitmap.addElement(tabBitmap[2]);
        }

        Log.i(TAG, "generateStringsBitmaps : Liczba zdj: " +String.valueOf(listBitmap.size()));

        if(listBitmap.size() == 1) image1 = generateStringImage(listBitmap.elementAt(0));
        if(listBitmap.size() == 2)  {
            image1 = generateStringImage(listBitmap.elementAt(0));
            image2 = generateStringImage(listBitmap.elementAt(1));
        }
        if(listBitmap.size() == 3) {
            image1 =generateStringImage(listBitmap.elementAt(0));
            image2 =generateStringImage(listBitmap.elementAt(1));
            image3 = generateStringImage(listBitmap.elementAt(2));
        }
    }

    private void pokazDialogPhoto() {
        mDialog = new Dialog(this);
        LayoutInflater li = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.dialog_add_photo_second, null, false);
        mDialog.setContentView(view);
        mDialog.setCancelable(true);

//        Log.d(TAG, "pokazDialogPhoto: Po dodaniu xmla");

        Button btnReturn = (Button) mDialog.findViewById(R.id.btn_return_wybor_zdj);
           imageViewFromGallery = (ImageView) mDialog.findViewById(R.id.iv_from_gallery);
        imageViewFromCamera = (ImageView) mDialog.findViewById(R.id.iv_from_camera);
//        Log.d(TAG, "pokazDialogPhoto: po przypisanach");

        imageViewFromGallery.setOnClickListener(v -> openGallery());
        imageViewFromCamera.setOnClickListener(v -> {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            }
            else
            {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            mDialog.dismiss();
        });

        btnReturn.setOnClickListener(v -> mDialog.cancel());

//        Log.d(TAG, "pokazDialogPhoto: przed getwindow");
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        /*Log.d(TAG, "PokazDialog: przed show");
        Log.d(TAG, "PokazDialog: pokazuje dialog:" + mDialog);*/
        mDialog.show();
    }

    private void openGallery() {
        Intent gallery = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
        mDialog.dismiss();
    }

    private int sprawdzPoprawnosc(){
        if(editTextAdult.getText().toString().equals(0)&&editTextChlid.getText().toString().equals(0)) return 1;
        if(editTextAdult.getText().toString().equals(0)&&editTextChlid.getText().toString()==null) return 1;
        if(editTextAdult.getText().toString()==null&&editTextChlid.getText().toString().equals(0)) return 1;
        if(editTextAdult.getText().toString()==null&&editTextChlid.getText().toString()==null) return 1;
        if (radioButtonLive.isChecked()==true){
            typeToReq =1;
        }else{
            typeToReq =0;
            if (tabBitmap[0]==null&&tabUri[0]==null) return 2;
        }
        return 0;
    }

    private void checkLocationPerms() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(location -> {
                            if(location != null) {
                                lat1 = location.getLatitude();
                                lon1 = location.getLongitude();

                                Log.i(TAG, "mamy localizaje" + String.valueOf(lat1) + " " + String.valueOf(lon1));
                            }
                        }).addOnFailureListener(e -> Log.i(TAG, "nie mamy lokalizaci"));
            }else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            //------------------TUTAJ MASZ SWOJE URI--------------------
            Uri imageUri = data.getData();

            //---------------------------------------------------------
            switch (nrZdj){
                case 0:
                    imageView1.setImageURI(imageUri);
                    tabUri[nrZdj]= imageUri;
                    imageView2.setVisibility(View.VISIBLE);
                    imageView2.setClickable(true);

                    break;
                case 1:
                    imageView2.setImageURI(imageUri);
                    tabUri[nrZdj]= imageUri;
                    imageView3.setVisibility(View.VISIBLE);
                    imageView3.setClickable(true);
                    break;
                case 2:
                    imageView3.setImageURI(imageUri);
                    tabUri[nrZdj]= imageUri;
                    break;
            }

        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Uri imageUri = data.getData();
            Log.d(TAG, "onActivityResult: to jest uri:"+imageUri);
            switch (nrZdj){
                case 0:
                    tabBitmap[0]=(Bitmap) data.getExtras().get("data");
                    imageView1.setImageBitmap(tabBitmap[0]);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView2.setClickable(true);

                    break;
                case 1:
                    tabBitmap[1]=(Bitmap) data.getExtras().get("data");
                    imageView2.setImageBitmap(tabBitmap[1]);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView3.setClickable(true);
                    break;
                case 2:
                    tabBitmap[2]=(Bitmap) data.getExtras().get("data");
                    imageView3.setImageBitmap(tabBitmap[2]);
                    break;
            }

        }
    }
}