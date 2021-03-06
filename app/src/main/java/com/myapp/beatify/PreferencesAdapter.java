package com.myapp.beatify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PreferencesAdapter extends RecyclerView.Adapter<PreferencesAdapter.MyViewHolder> {
    //Adapter for recycler view

    private List<CreatePreferences> tList;
    private OnItemClickListener listener;

    public PreferencesAdapter(List<CreatePreferences> tList) {
        this.tList = tList;
    }//constructor ends

    public interface OnItemClickListener {
        void onItemClick(int position);
    }//OnItemClickListener ends

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }//setOnItemClickListener ends

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_preferences, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view, listener);
        return viewHolder;
    }//onCreateViewHolder ends

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imgTxt.setText(tList.get(position).getTxt());
        holder.imgTxt.setBackgroundResource(tList.get(position).getImg());
    }//onBindViewHolder ends

    @Override
    public int getItemCount() {
        return tList.size();
    }//getItemCount ends

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        //View holder class
        public TextView imgTxt;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgTxt = itemView.findViewById(R.id.genreImgTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }//if ends
                    }//outer if ends

                }//onClick ends
            });
        }//constructor ends
    }//inner class ends
}//class ends
