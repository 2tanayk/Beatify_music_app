package com.myapp.beatify;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class SearchChildFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference musicRef = db.collection("Music");

    private RecyclerView searchRecyclerView;
    private SearchFragmentAdapter sAdapter;

    private SearchView searchView;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("SearchChildFragment", "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_child, container, false);
        Log.e("SearchChildFragment", "onCreateView()");
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e("SearchChildFragment", "onAttach()");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("SearchChildFragment", "onViewCreated()");
        searchView = view.findViewById(R.id.searchBox);

        createUserSearchRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                querySong(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void querySong(String s) {
        Log.e("val",s+"");
        Query query = musicRef.whereEqualTo("title", s);

        FirestoreRecyclerOptions<Music> options = new FirestoreRecyclerOptions.Builder<Music>()
                .setQuery(query, Music.class)
                .build();

        sAdapter = new SearchFragmentAdapter(options);
        sAdapter.startListening();
        searchRecyclerView.setAdapter(sAdapter);

        sAdapter.setOnItemClickListener(
                new SearchFragmentAdapter.OnItemClickListener() {//not working
                    @Override
                    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                        Log.e("Infosa", "Connected");
                        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();

                        Music music = documentSnapshot.toObject(Music.class);
                        String musicUrl = music.getMusicUrl();

                        //playMusic(musicUrl);
                    }
                });
    }

    private void createUserSearchRecyclerView() {
        // if(s=null)
        Query query = musicRef.whereEqualTo("title", "null");

        FirestoreRecyclerOptions<Music> options = new FirestoreRecyclerOptions.Builder<Music>()
                .setQuery(query, Music.class)
                .build();

        searchRecyclerView = view.findViewById(R.id.searchRV);
        searchRecyclerView.setHasFixedSize(true);

        searchRecyclerView.setNestedScrollingEnabled(false);

        sAdapter = new SearchFragmentAdapter(options);
        Log.e("SearchChildFragment", "" + sAdapter);


        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        searchRecyclerView.setAdapter(sAdapter);

        sAdapter.setOnItemClickListener(
                new SearchFragmentAdapter.OnItemClickListener() {//not working
                    @Override
                    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                        Log.e("Infosa", "Connected");
                        Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();

                        Music music = documentSnapshot.toObject(Music.class);
                        String musicUrl = music.getMusicUrl();

                        //playMusic(musicUrl);
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("SearchChildFragment", "onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("SearchChildFragment", "onStart()");
        sAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("SearchChildFragment", "onStop()");
        sAdapter.stopListening();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("SearchChildFragment", "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("SearchChildFragment", "onPause()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("SearchChildFragment", "onDetach()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SearchChildFragment", "onDestroy()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("SearchChildFragment", "onDestroyView()");
    }

}