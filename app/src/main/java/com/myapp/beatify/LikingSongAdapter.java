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

public class LikingSongAdapter extends FirestoreRecyclerAdapter<Music, LikingSongAdapter.MyViewHolder> {
    //RecyclerView.Adapter<LikingSongAdapter.MyViewHolder>
//    private List<CreateSong> mSongList;
    private OnItemClickListener mListener;
    private OnSongLikeListener lListener;

    private boolean lFlag = false;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public LikingSongAdapter(@NonNull FirestoreRecyclerOptions<Music> options) {
        super(options);
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface OnSongLikeListener {
        void onSongLike(DocumentSnapshot documentSnapshot, int position);
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_user_liking, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, mListener, lListener);
        return myViewHolder;
    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
////        holder.imageView.setImageResource(mSongList.get(position).getImgURL());
//        Glide.with(holder.imageView.getContext()).load(mSongList.get(position).getImgURL()).into(holder.imageView);
//        holder.textView.setText(mSongList.get(position).getTxt());
//    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Music model) {
        holder.textView.setText(model.getTitle());
        Glide.with(holder.imageView.getContext()).load(model.getUrl()).into(holder.imageView);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ImageView lImgView;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener, final OnSongLikeListener likeListener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.likingImg);
            this.textView = itemView.findViewById(R.id.nameTxt);
            this.lImgView = itemView.findViewById(R.id.likeImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(getSnapshots().getSnapshot(position), position);
                        }//if ends

                    }//outer if ends
                }//onClick ends
            });

            lImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (likeListener != null) {
                        int position = getAdapterPosition();

                        if (!lFlag) {
                            lFlag = true;
                            lImgView.setImageResource(R.drawable.ic_heart_fill);
                        } else {
                            lFlag = false;
                            lImgView.setImageResource(R.drawable.ic_heart_unfill);
                        }//else ends

                        if (position != RecyclerView.NO_POSITION) {
                            likeListener.onSongLike(getSnapshots().getSnapshot(position), position);
                        }//if ends

                    }//outer if ends
                }//onClick ends

            });
        }
    }
}
