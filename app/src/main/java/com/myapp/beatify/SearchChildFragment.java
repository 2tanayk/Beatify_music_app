package com.myapp.beatify;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SearchChildFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("SearchChildFragment", "onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_child, container, false);
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
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("SearchChildFragment", "onStop()");
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