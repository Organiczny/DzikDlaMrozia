package com.example.dzik;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;
import static com.example.dzik.DataManager.SHARED_PREFS;
import static com.example.dzik.DataManager.UNIQ_ID;
import static com.example.dzik.DataManager.UNIQ_ID_DEF;
import static com.example.dzik.R.id.frag_account_but_change;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragAccount#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragAccount extends Fragment {

    private static final String TAG = "FK_ACTIVITY";

    EditText et_nick;
    EditText et_email;
    EditText et_code;
    boolean checkerButChange = true;

    String et_nick_str;
    String et_email_str;
    String et_code_str;

    TextView tv_error;

    int et_code_int;

    Button but_change;
    Button but_commit;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragAccount() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment account.
     */
    // TODO: Rename and change types and number of parameters
    public static FragAccount newInstance(String param1, String param2) {
        FragAccount fragment = new FragAccount();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_account, container, false);

        et_nick = v.findViewById(R.id.frag_account_et_nick);
        et_email= v.findViewById(R.id.frag_account_et_email);
        et_code= v.findViewById(R.id.frag_account_et_code);
        tv_error = v.findViewById(R.id.tv_error);
        but_change = v.findViewById(R.id.frag_account_but_change);
        but_commit = v.findViewById(R.id.frag_account_but_commit);

        setValueInET();

        disableEditText(et_nick);
        disableEditText(et_email);
        disableEditText(et_code);
        but_commit.setVisibility(View.INVISIBLE);

        but_change.setOnClickListener(v1 -> {

            Log.i(TAG, but_change.getText().toString());
            if(checkerButChange){
                enableEditText(et_nick);
                enableEditText(et_email);
                enableEditText(et_code);
                but_commit.setVisibility(View.VISIBLE);
                but_change.setText("Anuluj");
                checkerButChange= !checkerButChange;
            }else if(!checkerButChange){
                disableEditText(et_nick);
                disableEditText(et_email);
                disableEditText(et_code);
                but_commit.setVisibility(View.INVISIBLE);
                but_change.setText("Zmien");
                checkerButChange= !checkerButChange;
            }

        });

        but_commit.setOnClickListener(v1 -> {
            setData();
        });

        return v;
    }

    private void setData() {

        generateEditTextValues();

        JsonPlaceHolderApi jsonPlaceHolderApi;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zefiro.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String uniq =sp.getString(UNIQ_ID, UNIQ_ID_DEF);

        Log.i(TAG, uniq);

        PostUpDataSet postUpDataSet = new PostUpDataSet("updset",uniq,  et_nick_str, et_email_str, et_code_int);
        Call<PostUpDataSet> call = jsonPlaceHolderApi.createPostUpDataSet(postUpDataSet);

        call.enqueue(new Callback<PostUpDataSet>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PostUpDataSet> call, Response<PostUpDataSet> response) {
                if(!response.isSuccessful()) {
                    Toast("Błąd Code: " + response.code(), 0);
                    return;
                }
                PostUpDataSet postUpDataSet = response.body();
                Log.i(TAG,  postUpDataSet.getResponse());
                if(!(postUpDataSet.getResponse() == null)) {
                    if (postUpDataSet.getResponse().equals("wrong_uniq")) startActivity(new Intent(getActivity(), AcRegister.class));
                }
                if(postUpDataSet.getResponse().equals("false")){
                    Toast("Nie zapisano danych", 0);
                    return;
                }
                if(postUpDataSet.getResponse().equals("true")){
                    hideButtonsIfResCorrect();
                    Toast("Zapisano dane poprawnie",1 );
                    return;
                }
            }

            @Override
            public void onFailure(Call<PostUpDataSet> call, Throwable t) {
                Log.i(TAG, "ERROR RESP");
                Toast("ERROR: " + t.getMessage(), 0);
            }
        });

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

    private void hideButtonsIfResCorrect() {
        disableEditText(et_nick);
        disableEditText(et_email);
        disableEditText(et_code);
        but_commit.setVisibility(View.INVISIBLE);
        but_change.setText("Zmien");
        checkerButChange= !checkerButChange;
    }

    private void setValueInET() {

        JsonPlaceHolderApi jsonPlaceHolderApi;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zefiro.pl/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        SharedPreferences sp = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        Log.i(TAG, sp.getString(UNIQ_ID, UNIQ_ID_DEF));

        PostGetSet postGetSet = new PostGetSet("getset", sp.getString(UNIQ_ID, UNIQ_ID_DEF));
        Call<PostGetSet> call = jsonPlaceHolderApi.createPostGetSet(postGetSet);

        call.enqueue(new Callback<PostGetSet>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<PostGetSet> call, Response<PostGetSet> response) {
                if(!response.isSuccessful()) {
                    tv_error.setText("Code: " + response.code());
                    return;
                }
                PostGetSet postGetSet = response.body();
                Log.i(TAG, postGetSet.getName() + " " + postGetSet.getMail() + " " + String.valueOf(postGetSet.getCode()) + " " + postGetSet.getResponse());

                if(!(postGetSet.getResponse() == null)){
                    tv_error.setText("Bład podczas wczytywania danych z servera");
                    Log.i(TAG, "Bład podczas wczytywania danych z servera");
                    return;
                }

                et_nick.setText(postGetSet.getName());
                et_email.setText(postGetSet.getMail());
                et_code.setText(String.valueOf(postGetSet.getCode()));

            }

            @Override
            public void onFailure(Call<PostGetSet> call, Throwable t) {
                Log.i(TAG, "ERROR RESP");
                tv_error.setText("ERROR: " + t.getMessage());
            }
        });

    }

    private void generateEditTextValues() {
        et_nick_str = et_nick.getText().toString();
        et_email_str = et_email.getText().toString();
        et_code_str = et_code.getText().toString();
        et_code_int = Integer.parseInt(et_code_str);
    }


    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
    }

    private void enableEditText(EditText editText) {
        editText.setFocusableInTouchMode(true);
        editText.setEnabled(true);
        editText.setCursorVisible(true);

    }


}