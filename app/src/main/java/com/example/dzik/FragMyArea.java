package com.example.dzik;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragMyArea#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragMyArea extends Fragment {

    String TAG="FK_ACTIVITY";
    double lat1, lon1;

    FusedLocationProviderClient fusedLocationProviderClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragMyArea() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment my_area.
     */
    // TODO: Rename and change types and number of parameters
    public static FragMyArea newInstance(String param1, String param2) {
        FragMyArea fragment = new FragMyArea();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v =inflater.inflate(R.layout.frag_my_area, container, false);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        checkLocationPerms();

        Button but = v.findViewById(R.id.reset);
//        but.setVisibility(View.INVISIBLE);
        but.setOnClickListener(v1 -> {
            Toast("Wylogowano", 1);
            SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(UNIQ_ID, "null1");
            editor.putString(UNIQ_ID_DEF, "null1");
            editor.apply();
        });

        request();

        return v;
    }

    private void request() {

        JsonPlaceHolderApi jsonPlaceHolderApi;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zefiro.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        Log.i(TAG, "uniq= "+ sp.getString(UNIQ_ID, UNIQ_ID_DEF));

        PostArea postArea = new PostArea("area", sp.getString(UNIQ_ID, UNIQ_ID_DEF), lat1, lon1);
        Call<List<PostArea>> call = jsonPlaceHolderApi.createPostArea(postArea);

        call.enqueue(new Callback<List<PostArea>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<PostArea>> call, Response<List<PostArea>> response) {
                if(!response.isSuccessful()) {
                    Log.i(TAG, "Code: " + response.code());
                    Toast("Code: " + response.code(), 0);
                    return;
                }

                List<PostArea> list = response.body();
                if(list.size() >0 ){
                    if(!(list.get(0).getResponse() == null)){
                        if(list.get(0).getResponse().equals("wrong_uniq")) startActivity(new Intent(getActivity(), AcRegister.class));
                    }
                }


                Log.i(TAG, "Ilosc zgloszen= " + String.valueOf(list.size()));

                if(list.size() == 0) {
                    Toast("Nie masz żadnych zgłoszeń w twojej okolicy", 0);
                    Log.i(TAG, "Nie masz żadnych zgłoszeń");
                    return;
                }

//                Vector<String> opisy = new Vector<String>() ;
//                Vector<String> data= new Vector<String>();
//                Vector<String> status= new Vector<String>();
//                Vector<String> miejsce= new Vector<String>();
//                Vector<Integer> ileMlodych= new Vector<Integer>();
//                Vector<Integer> ileStarych= new Vector<Integer>();


//                for(int i=0; i<list.size() ;i++) {
//                    opisy.addElement(list.get(i).getDescrip());
//                    data.addElement(list.get(i).getDate());
//                    status.addElement(list.get(i).getStatus());
//                    miejsce.addElement(list.get(i).getLoc_pow());
//                    ileMlodych.addElement(list.get(i).getNum_young());
//                    ileStarych.addElement(list.get(i).getNum_old());
//                }

                /*opisy.addElement("Pierwszy Opis");
                data.addElement("27.11.2020");
                status.addElement("Oczekujący");
                miejsce.addElement("Zabrze");
                ileMlodych.addElement(2);
                ileStarych.addElement(1);*/

//                HistoryAdapter historyAdapter = new HistoryAdapter(context,opisy,data,status,miejsce,ileMlodych,ileStarych);
//        Log.d(TAG, "onCreateView: po stworzeniu"+historyAdapter);
//                recyclerView.setAdapter(historyAdapter);
//        Log.d(TAG, "onCreateView: po adapterze"+recyclerView);
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        Log.d(TAG, "onCreateView: po layout manager"+recyclerView);


            }

            @Override
            public void onFailure(Call<List<PostArea>> call, Throwable t) {
                Log.i(TAG, "ERROR RESP"+ t.getMessage());
                Toast("ERROR RESP"+ t.getMessage(), 0);
            }
        });
    }

    private void checkLocationPerms() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(getContext().getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED) {
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

    private void Toast(String text1, int color) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) getActivity().findViewById(R.id.toast_layout));

        TextView text = layout.findViewById(R.id.tv_toast);
        text.setText(text1);
        if(color == 0) {
            layout.setBackgroundResource(R.drawable.toast_bg_red);
        }
        if(color == 1) {
            layout.setBackgroundResource(R.drawable.toast_bg_green);
        }

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}