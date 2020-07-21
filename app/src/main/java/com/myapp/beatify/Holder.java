package com.myapp.beatify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Holder extends Fragment {
    //Fragment for homepage
    View view;
    BottomNavigationView bottomNavigationView;
    private static Fragment childFragment;
//    private static FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }//onCreate ends

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view=inflater.inflate(R.layout.fragment_holder_fragment, container, false);

          return view;
    }//onCreateView ends

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bottomNavigationView=view.findViewById(R.id.bottom_nav_bar);
        //set default as home page
        childFragment = new HomeChildFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.frag_holder, childFragment).commit();

        switchPage();
    }

    private void switchPage() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.nav_home:
                        childFragment = new HomeChildFragment();
                        break;
                    case R.id.nav_search:
                        childFragment = new SearchChildFragment();
                        break;
                    case R.id.nav_liked:
                        childFragment = new FavouritesChildFragment();
                        break;
                    default:
                        Toast.makeText(getContext(), "oops :(", Toast.LENGTH_SHORT).show();
            }

                getChildFragmentManager().beginTransaction().replace(R.id.frag_holder,
                        childFragment).commit();

                return true;
            }
        });
    }
}//class ends






//        return view;