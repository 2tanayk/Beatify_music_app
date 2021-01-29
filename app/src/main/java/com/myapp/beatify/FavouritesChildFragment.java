package com.myapp.beatify;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;

public class FavouritesChildFragment extends Fragment {
    View view;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference userDoc = db.collection("Users").
            document("" + FirebaseAuth.getInstance().getCurrentUser().getUid())
            .collection("Favourites");

    private RecyclerView favouritesRecyclerView;
    private FavouritesFragmentAdapter favouritesFragmentAdapter;

    MediaPlayer player;
    private SeekBar mSeekBar;
    private ImageView mControlImageView;
    private ImageView bImageView;
    private Handler mHandler = new Handler();

    boolean flag = true;

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

        mSeekBar = ((HostFragment) getParentFragment()).seekBar;
        mControlImageView = ((HostFragment) getParentFragment()).controlImageView;
        bImageView = ((HostFragment) getParentFragment()).bottomImg;

        createUserFavouritesRecyclerView();


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    player.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mControlImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    Log.e("info", "if block");
                    mControlImageView.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    player.pause();
                    flag = false;
                } else {
                    Log.e("info", "else block");
                    mControlImageView.setImageResource(R.drawable.ic_baseline_pause_24);
                    player.start();
                    flag = true;
                }//else ends

            }//onClick ends
        });
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

                Music music = documentSnapshot.toObject(Music.class);

                String musicUrl = music.getMusicUrl();
                String url = music.getUrl();

                ((HostFragment) getParentFragment()).bottomText.setText(music.getTitle());
                Glide.with(getActivity()).load(url).into(bImageView);

                playMusic(musicUrl);

            }
        });

    }

    private void playMusic(String musicUrl) {
        if (player == null) {
            Log.e("InfoFF", "creating player");
            ((HostFragment) getParentFragment()).bottomHelper.setVisibility(View.VISIBLE);
            //player = new MediaPlayer();
            player = ((HostFragment) getParentFragment()).player;

            if (player.isPlaying()) {
                player.reset();
            }

            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } else {
            Log.e("InfoFCF", "resetting player");
            player.reset();
        }

        try {
            Log.e("InfoFCF", "preparing player async");
            player.setDataSource(musicUrl);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(final MediaPlayer mediaPlayer) {
                Log.e("InfoFF", "starting player");

                mSeekBar.setMax(mediaPlayer.getDuration());
                mSeekBar.setProgress(0);

                mControlImageView.setImageResource(R.drawable.ic_baseline_pause_24);
                flag=true;


                mediaPlayer.start();
                MediaEventBus.getInstance().postFragmentAction(new MediaEvent(MediaEventBus.ACTION_MUSIC_PLAYED_FROM_FRAGMENT, player));
                Log.e("InfoHCF", "Working!!");

                if (mediaPlayer.isPlaying()) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            mSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                            mHandler.postDelayed(this, 1000);
                        }
                    };
                    mHandler.postDelayed(runnable, 1000);
                }//if ends
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