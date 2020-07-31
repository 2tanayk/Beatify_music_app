package com.myapp.beatify;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeChildFragment extends Fragment {
    private List<CreateSong> tList;
    private List<CreateSong> recentList;

    private RecyclerView tRecyclerView;
    private RecyclerView recentRecyclerView;

    private RecyclerView.Adapter tAdapter;
    private RecyclerView.Adapter rAdapter;

//    private RecyclerView.LayoutManager tLayoutManager;

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createUserLikingList();
        createUserRecentList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home_child, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createUserLikingRecyclerView();
        createUserRecentRecyclerView();
    }


    private void createUserLikingList() {
        tList = new ArrayList<>();

        tList.add(new CreateSong(R.drawable.app_logo, "Waiting For Tomorrow"));
        tList.add(new CreateSong(R.drawable.app_logo, "Gold Skies"));
        tList.add(new CreateSong(R.drawable.app_logo, "Never Let Me Go"));
        tList.add(new CreateSong(R.drawable.app_logo, "Happy Now"));
        tList.add(new CreateSong(R.drawable.app_logo, "Titanium"));
        tList.add(new CreateSong(R.drawable.app_logo, "Sing Me To Sleep"));
    }

    private void createUserRecentList() {
        recentList = new ArrayList<>();

        recentList.add(new CreateSong(R.drawable.app_logo, "Waiting For Tomorrow"));
        recentList.add(new CreateSong(R.drawable.app_logo, "Gold Skies"));
        recentList.add(new CreateSong(R.drawable.app_logo, "Never Let Me Go"));
        recentList.add(new CreateSong(R.drawable.app_logo, "Happy Now"));
        recentList.add(new CreateSong(R.drawable.app_logo, "Titanium"));
        recentList.add(new CreateSong(R.drawable.app_logo, "Sing Me To Sleep"));
    }

    private void createUserLikingRecyclerView() {
        tRecyclerView = view.findViewById(R.id.user_liking_RV);
        tRecyclerView.setHasFixedSize(true);
        tAdapter = new SongAdapter(tList);

        tRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        tRecyclerView.setAdapter(tAdapter);
    }

    private void createUserRecentRecyclerView() {
        recentRecyclerView = view.findViewById(R.id.user_recent_RV);
        recentRecyclerView.setHasFixedSize(true);
        rAdapter = new SongAdapter(recentList);

        recentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recentRecyclerView.setAdapter(rAdapter);
    }
}