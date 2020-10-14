package com.myapp.beatify;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

//we implement the interface to handle onClick() from the nav. drawer
public class HostFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {
    //Fragment for homepage
    View view;
    public LinearLayout bottom;
    public TextView bottomText;

    //public TextView bTxt;
    protected BottomNavigationView bottomNavigationView;
    protected NavigationView navigationView;

    //private static Fragment childFragment;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;

//    private static FragmentTransaction transaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("Host Fragment", "onCreate()");

    }//onCreate ends

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_holder_fragment, container, false);
        Log.e("Host Fragment", "onCreateView()");

        return view;
    }//onCreateView ends

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.e("Host Fragment", "onViewCreated()");

        Toolbar toolbar = view.findViewById(R.id.toolbar); //Toolbar enabled for the hamburger icon (for navigation drawer)

        drawerLayout = view.findViewById(R.id.homeFrag);//DrawerLayout for nav. drawer
        navigationView = view.findViewById(R.id.nav_view);//navigation view for the drawer

        bottom = view.findViewById(R.id.bottomLL);//for the bottom media player
        //bTxt = view.findViewById(R.id.bottomTxt);
        bottomText = view.findViewById(R.id.bottomTxt);//a part of the bottom media player for the receiving onClick()

        //to enable sliding in and out on the nav drawer and creating a hamburger icon
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        bottomNavigationView = view.findViewById(R.id.bottom_nav_bar);//bottom navigation view to switch from home page to other

        //set home page as default

        //childFragment =new HomeChildFragment();
        fragmentManager = getChildFragmentManager();//to nest fragments inside this fragment
        fragmentManager.beginTransaction().add(R.id.frag_holder, new HomeChildFragment(), "home").addToBackStack("holder").commit();

        bottomText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MediaPlayerActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), bottom, ViewCompat.getTransitionName(bottom));
                startActivity(intent, options.toBundle());
            }
        });
        navigationView.setNavigationItemSelectedListener(this);//to receive onClick() from contents inside the drawer

        switchPage();//method to switch between different pages (fragments)
    }


    private void switchPage() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {//switch case to receive and execute appropriate onClick() events from bottom nav.view
                    case R.id.nav_home://home page
                        if (fragmentManager.findFragmentByTag("home") != null) { //we show the home fragment
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("home")).commit();
                        }
                        //we hide the other two
                        if (fragmentManager.findFragmentByTag("search") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("search")).commit();
                        }
                        if (fragmentManager.findFragmentByTag("liked") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("liked")).commit();
                        }
                        break;
                    case R.id.nav_search: //search page
                        //we add if it has not already been added else we show it
                        if (fragmentManager.findFragmentByTag("search") != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("search")).commit();
                        } else {
                            fragmentManager.beginTransaction().add(R.id.frag_holder, new SearchChildFragment(), "search").addToBackStack("holder").commit();
                        }
                        //we hide the other two
                        if (fragmentManager.findFragmentByTag("home") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")).commit();
                        }
                        if (fragmentManager.findFragmentByTag("liked") != null) {
                            //if the other fragment is visible, hide it.
                            fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("liked")).commit();
                        }

                        break;
                    case R.id.nav_liked://user liked songs page
                        //we add if it has not already been added else we show it
                        if (fragmentManager.findFragmentByTag("liked") != null) {
                            fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("liked")).commit();
                        } else {
                            fragmentManager.beginTransaction().add(R.id.frag_holder, new FavouritesChildFragment(), "liked").addToBackStack("holder").commit();
                        }
                        //we hide the other two
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
                }//end of switch

//                getChildFragmentManager().beginTransaction().replace(R.id.frag_holder,
//                        childFragment).commit();

                return true;
            }//method end
        });
    }//end of the switch page method

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {//switch case to recieve and execute appropriate onClick() events from nav. drawer
            case R.id.nav_mode://to switch between dark and light theme
                break;
            case R.id.nav_settings://to change user name and dp
                ((MainActivity) getActivity()).hideHost();
                break;
            case R.id.nav_share://to share the app
                break;
            case R.id.nav_logout://to logout from the app
                // Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
                ((MainActivity) getActivity()).logOut();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("Host Fragment", "onAttach()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("Host Fragment", "onStart()");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("Host Fragment", "onStop()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Host Fragment", "onResume()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Host Fragment", "onActivityCreated()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("Host Fragment", "onPause()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Host Fragment", "onDetach()");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Host Fragment", "onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Host Fragment", "onDestroyView()");
    }


    //    public void openMediaPlayer(View view) {
//        try {
//
////            Toast.makeText(getContext(), "oops:(", Toast.LENGTH_SHORT).show();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "oops:(", Toast.LENGTH_SHORT).show();
//        }
//    }


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