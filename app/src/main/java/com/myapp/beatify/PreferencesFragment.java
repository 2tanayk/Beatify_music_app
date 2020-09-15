package com.myapp.beatify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PreferencesFragment extends Fragment {
    //Fragment FOR music preference page
//    private List<String> pref;
    private List<CreatePreferences> clist;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private PreferencesAdapter mAdapter;

    //public static int posn;
    View view;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_preferences, container, false);
        createRecyclerView();
        return view;
    }//onCreateView ends

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createList();
    }//onCreate ends


    private void createList() {
        clist = new ArrayList<>();


        clist.add(new CreatePreferences("Heavy Metal", R.drawable.heavymetal));
        clist.add(new CreatePreferences("Bollywood", R.drawable.bollywoodmusic));
        clist.add(new CreatePreferences("EDM", R.drawable.edm));
        clist.add(new CreatePreferences("Singles", R.drawable.singles));
        clist.add(new CreatePreferences("Band", R.drawable.bands));
        clist.add(new CreatePreferences("Rap", R.drawable.rap));

    }//createList ends

    private void createRecyclerView() {
        mRecyclerView = view.findViewById(R.id.prefRV);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new PreferencesAdapter(clist);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new PreferencesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                posn = position;

//                recordPref = clist.get(position).getTxt();
                ((MainActivity) getActivity()).setRecordPref(clist.get(position).getTxt());// we pass this to the parent activity

                ((MainActivity) getActivity()).onGenreClicked();// to get to our home page (we first add a host fragment through this)
//                Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }//onItemClick ends
        });


    }//createRecyclerView ends
}//class ends