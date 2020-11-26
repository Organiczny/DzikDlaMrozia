package com.example.dzik;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AcMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);

        BottomNavigationView bottomNavigationView  = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        bottomNavigationView.setSelectedItemId(R.id.main);
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