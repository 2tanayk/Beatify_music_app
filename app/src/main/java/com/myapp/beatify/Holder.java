package com.myapp.beatify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
    private static FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
//    private static FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }//onCreate ends

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_holder_fragment, container, false);


        return view;
    }//onCreateView ends

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        drawerLayout = view.findViewById(R.id.homeFrag);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        bottomNavigationView = view.findViewById(R.id.bottom_nav_bar);
        //set default as home page
        //childFragment =new HomeChildFragment();
        fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frag_holder, new HomeChildFragment(), "home").addToBackStack("holder").commit();

        switchPage();
    }


    private void switchPage() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        if (fragmentManager.findFragmentByTag("home") != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("home")).commit();
                        }
                        if (fragmentManager.findFragmentByTag("search") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("search")).commit();
                        }
                        if (fragmentManager.findFragmentByTag("liked") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("liked")).commit();
                        }
                        break;
                    case R.id.nav_search:
                        //childFragment = new SearchChildFragment();
                        if (fragmentManager.findFragmentByTag("search") != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("search")).commit();
                        } else {
                            fragmentManager.beginTransaction().add(R.id.frag_holder, new SearchChildFragment(), "search").addToBackStack("holder").commit();
                        }

                        if (fragmentManager.findFragmentByTag("home") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")).commit();
                        }
                        if (fragmentManager.findFragmentByTag("liked") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("liked")).commit();
                        }

                        break;
                    case R.id.nav_liked:
                        if (fragmentManager.findFragmentByTag("liked") != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("liked")).commit();
                        } else {
                            fragmentManager.beginTransaction().add(R.id.frag_holder, new FavouritesChildFragment(), "liked").addToBackStack("holder").commit();
                        }
                        if (fragmentManager.findFragmentByTag("home") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")).commit();
                        }
                        if (fragmentManager.findFragmentByTag("search") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("search")).commit();
                        }

                        break;
                    default:
                        Toast.makeText(getContext(), "oops :(", Toast.LENGTH_SHORT).show();
                }

//                getChildFragmentManager().beginTransaction().replace(R.id.frag_holder,
//                        childFragment).commit();

                return true;
            }
        });
    }


}//class ends


// childFragment = new HomeChildFragment();
//!(fragmentManager.findFragmentByTag("home").isVisible())
//&& fragmentManager.findFragmentByTag("search").isVisible()
//fragmentManager.findFragmentByTag("liked") != null &&
//!(fragmentManager.findFragmentByTag("search").isVisible()) &&
// if( fragmentManager.findFragmentByTag("search") ==null)
//                        {
//
//&& fragmentManager.findFragmentByTag("home").isVisible()                        }
//&& fragmentManager.findFragmentByTag("liked").isVisible()
//!(fragmentManager.findFragmentByTag("liked").isVisible()) &&
//&& fragmentManager.findFragmentByTag("search").isVisible()
//