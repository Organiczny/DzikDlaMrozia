package com.example.dzik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static com.example.dzik.DataManager.SHARED_PREFS;
import static com.example.dzik.DataManager.UNIQ_ID;
import static com.example.dzik.DataManager.UNIQ_ID_DEF;

public class AcMain extends AppCompatActivity {

    private static final String TAG = "FK_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        autoLogin();

        BottomNavigationView bottomNavigationView  = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.main);
    }

    private void autoLogin() {
        SharedPreferences sp = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        if(sp.getString(UNIQ_ID, "2").equals(sp.getString(UNIQ_ID_DEF, "11"))) {
            startActivity(new Intent(AcMain.this, AcRegister.class));
        }
        Log.i(TAG, "AUTOLOGIN: " +sp.getString(UNIQ_ID, "1"));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                    Fragment selectedFrag = null;

                    switch(item.getItemId()) {
                        case R.id.main:
                            selectedFrag = new FragMain();
                            break;
                        case R.id.my_area:
                            selectedFrag = new FragMyArea();
                            break;
                        case R.id.account:
                            selectedFrag = new FragAccount();
                            break;
                        case R.id.historia_zgloszen:
                            selectedFrag = new FragHistory();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedFrag).commit();
                    return true;
                }
    };


}