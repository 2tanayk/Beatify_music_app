package com.myapp.beatify;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

public class TopSongsAdapter extends FirestoreRecyclerAdapter<Music, TopSongsAdapter.MyViewHolder> {
    //private List<CreateSong> mRecentSongList;
    private OnTopSongsItemClickListener mListener;
    private OnSongLikeListener lListener;

    private ListenerRegistration listenerRegistration;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    CollectionReference userFavouriteDoc = db.collection("Users").
            document("" + FirebaseAuth.getInstance().getCurrentUser().getUid())
            .collection("Favourites");

    private boolean lFlag = false;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TopSongsAdapter(@NonNull FirestoreRecyclerOptions<Music> options) {
        super(options);
    }

    public interface OnTopSongsItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public interface OnSongLikeListener {
        void onSongLike(DocumentSnapshot documentSnapshot, int position, boolean liked);
    }

    public void setOnItemClickListener(OnTopSongsItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemLikeListener(OnSongLikeListener listener) {
        lListener = listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_recent_songs, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, mListener, lListener);
        return myViewHolder;
    }//onCreateViewHolder ends

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, int position, @NonNull final Music model) {
        holder.textView.setText(model.getTitle());
        Glide.with(holder.imageView.getContext()).load(model.getUrl()).into(holder.imageView);

        DocumentReference pDoc = getSnapshots().
                getSnapshot(position).
                getReference().
                collection("Likes").
                document("" + FirebaseAuth.getInstance().getCurrentUser().getUid());


        pDoc.get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                          @Override
                                          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                              if (task.isSuccessful()) {
                                                  DocumentSnapshot document = task.getResult();
                                                  if (document.exists()) {
                                                      Log.e("LikingSongAdapter", "Document exists!");
                                                      holder.lImgView.setImageResource(R.drawable.ic_heart_fill);
                                                      holder.lImgView.setTag("l");

                                                      Log.e(model.getTitle(), holder.lImgView.getTag().toString());
                                                  } else {
                                                      Log.e("LikingSongAdapter", "Document does not exist!");
                                                  }//else ends
                                              } else {
                                                  Log.e("LikingSongAdapter", String.valueOf(task.getException()));
                                              }//else ends
                                          }//onComplete ends
                                      }
                );

    }//onBindViewHolder ends

    @Override
    public void startListening() {
        super.startListening();
        Log.e("infoTSA", "started listening");

        listenerRegistration = userFavouriteDoc.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("TAG", "listen:error", error);
                    return;
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:

                            break;
                        case MODIFIED:

                            break;
                        case REMOVED:
                            //dc.getDocument()
                            break;
                    }//switch ends
                }//for ends
            }//onEvent ends
        });

    }//startListening ends

    @Override
    public void stopListening() {
        super.stopListening();
        Log.e("infoTSA", "stopped listening");
        listenerRegistration.remove();

    }//stopListening ends

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public ImageView lImgView;
        //public CardView cardView;

        public MyViewHolder(@NonNull View itemView, final OnTopSongsItemClickListener listener, final OnSongLikeListener likeListener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.recent_img);
            this.textView = itemView.findViewById(R.id.recent_txt);
            this.lImgView = itemView.findViewById(R.id.likeRSImg);
            //this.cardView = itemView.findViewById(R.id.create_recent_RV);

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

            lImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = String.valueOf(lImgView.getTag());
                    if (likeListener != null) {
                        int position = getAdapterPosition();

                        if (tag.equals("nl")) {
                            lFlag = true;
                            lImgView.setTag("l");
                            lImgView.setImageResource(R.drawable.ic_heart_fill);
                            Log.e("LikingAdapterLogL", "imgTag " + String.valueOf(lImgView.getTag()) + " like " + lFlag);
                        } else {
                            lFlag = false;
                            lImgView.setTag("nl");
                            lImgView.setImageResource(R.drawable.ic_heart_unfill);
                            Log.e("LikingAdapterLogNL", "imgTag " + String.valueOf(lImgView.getTag()) + " like " + lFlag);
                        }//else ends

                        if (position != RecyclerView.NO_POSITION) {
                            likeListener.onSongLike(getSnapshots().getSnapshot(position), position, lFlag);
                        }
                    }//outer if ends

                }//onClick ends
            });
        }
    }

}
