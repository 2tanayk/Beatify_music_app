package com.myapp.beatify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class OurPicksAdapter extends FirestoreRecyclerAdapter<Music, OurPicksAdapter.MyViewHolder> {
    //private List<CreateSong> mOurPicksList;
    private OnItemClickListener mListener;
    private OnSongLikeListener lListener;

    private boolean lFlag = false;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public OurPicksAdapter(@NonNull FirestoreRecyclerOptions<Music> options) {
        super(options);
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface OnSongLikeListener {
        void onSongLike(DocumentSnapshot documentSnapshot, int position, boolean liked);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemLikeListener(OnSongLikeListener listener) {
        lListener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_our_picks, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, mListener,lListener);
        return myViewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Music model) {
        holder.textView.setText(model.getTitle());
        Glide.with(holder.imageView.getContext()).load(model.getUrl()).into(holder.imageView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ImageView lImageView;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener, final OnSongLikeListener likeListener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.our_img);
            this.textView = itemView.findViewById(R.id.our_txt);
            this.lImageView = itemView.findViewById(R.id.likeOPImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(getSnapshots().getSnapshot(position), position);
                        }
                    }
                }
            });

            lImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (likeListener != null) {
                        int position = getAdapterPosition();

                        likeListener.onSongLike(getSnapshots().getSnapshot(position), position, lFlag);
                    }

                }
            });


        }
    }
}

