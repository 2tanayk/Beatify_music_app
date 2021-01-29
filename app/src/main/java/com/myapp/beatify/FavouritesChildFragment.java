package com.myapp.beatify;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class FavouritesChildFragment extends Fragment {
    View view;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userDoc = db.collection("Users").
            document("" + FirebaseAuth.getInstance().getCurrentUser().getUid())
            .collection("Favourites");

    private RecyclerView favouritesRecyclerView;
    private FavouritesFragmentAdapter favouritesFragmentAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_favourites_child, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createUserFavouritesRecyclerView();
    }

    private void createUserFavouritesRecyclerView() {
        Query query = userDoc;

        FirestoreRecyclerOptions<Music> options = new FirestoreRecyclerOptions.Builder<Music>()
                .setQuery(query, Music.class)
                .build();

        favouritesRecyclerView = view.findViewById(R.id.favouriteSongsRV);
        favouritesRecyclerView.setHasFixedSize(true);
        favouritesFragmentAdapter = new FavouritesFragmentAdapter(options);

        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        favouritesRecyclerView.setAdapter(favouritesFragmentAdapter);


        favouritesFragmentAdapter.setOnItemClickListener(new FavouritesFragmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        favouritesFragmentAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        favouritesFragmentAdapter.stopListening();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}