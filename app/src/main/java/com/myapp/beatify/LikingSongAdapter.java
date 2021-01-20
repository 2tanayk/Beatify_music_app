package com.myapp.beatify;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


public class LikingSongAdapter extends FirestoreRecyclerAdapter<Music, LikingSongAdapter.MyViewHolder> {
    //RecyclerView.Adapter<LikingSongAdapter.MyViewHolder>
//    private List<CreateSong> mSongList;
    private OnItemClickListener mListener;
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

    public LikingSongAdapter(@NonNull FirestoreRecyclerOptions<Music> options) {
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
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_user_liking, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, mListener, lListener);
        return myViewHolder;
    }

    @Override
    public void startListening() {
        super.startListening();
        Log.e("infoLLA", "started listening");

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
        Log.e("infoLLA", "stopped listening");
        listenerRegistration.remove();

    }//stopListening ends

    //
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
////        holder.imageView.setImageResource(mSongList.get(position).getImgURL());
//        Glide.with(holder.imageView.getContext()).load(mSongList.get(position).getImgURL()).into(holder.imageView);
//        holder.textView.setText(mSongList.get(position).getTxt());
//    }

    @Override
    protected void onBindViewHolder(@NonNull final MyViewHolder holder, int position, @NonNull final Music model) {
        holder.textView.setText(model.getTitle());
        Glide.with(holder.imageView.getContext()).load(model.getUrl()).into(holder.imageView);
        //holder.lImgView.setTag("nl");

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
                                                      //lFlag = true;
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
                    String tag = String.valueOf(lImgView.getTag());
                    Log.e("imgTag", tag);

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
                        }//if ends

                    }//outer if ends
                }//onClick ends

            });

        }

    }//ViewHolder class ends
}//LikingSongAdapter ends
