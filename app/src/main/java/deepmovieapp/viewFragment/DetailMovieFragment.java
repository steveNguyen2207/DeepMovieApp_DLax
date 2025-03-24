package com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;
import com.vn.nguyenhoanganhtu.deepmovieapp.itemConfigAdapter.MovieAdapter;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.Favourite_History;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.Movie;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.User;
import com.vn.nguyenhoanganhtu.deepmovieapp.utils.CheckApplication;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity.HomePage;

public class DetailMovieFragment extends Fragment {
    private View view;
    private ExoPlayer player;
    private StyledPlayerView playerView;
    private TextView txtTitle;
    private TextView txtRelease;
    private TextView txtGenre;
    private TextView txtDirector;
    private TextView txtDescription;
    private ImageView imgYeuThich;
    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private Movie movie;

    void bindingView() {
        playerView = view.findViewById(R.id.player_view);
        txtTitle = view.findViewById(R.id.txtTitle);
        txtRelease = view.findViewById(R.id.txtRelease);
        txtGenre = view.findViewById(R.id.txtGenre);
        txtDirector = view.findViewById(R.id.txtDirector);
        txtDescription = view.findViewById(R.id.txtDescription);
        imgYeuThich = view.findViewById(R.id.imgYeuThich);
    }

    void bindingAction() {
        imgYeuThich.setOnClickListener(this::onClickBtnYeuThich);
    }

    private void onClickBtnYeuThich(View view) {

        //CheckApplication checkApplication = CheckApplication.getInstance();
        //boolean check = checkApplication.isCheck();

        Favourite_History newMovie = new Favourite_History();
        newMovie.setTitle(movie.getTitle());
        newMovie.setUrlImageMovie(movie.getUrlImageMovie());
        newMovie.setGenre(movie.getGenre());
        newMovie.setDirector(movie.getDirector());
        newMovie.setId(movie.getId());
        newMovie.setUserId(currentUser.getUid());

        firestore.collection("favourite")
                .add(newMovie).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getActivity(),
                                "Your add " + movie.getTitle() + " to Favourite Successfull!",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),
                                "Your add " + movie.getTitle() + " to Favourite Fail!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void setupContentMovie() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            movie = (Movie) bundle.getParcelable("movie");

            txtTitle.setText(movie.getTitle());
            txtRelease.setText("- Release: " + movie.getReleaseDate());
            txtGenre.setText("- Genre: " + movie.getGenre());
            txtDirector.setText("- Director: " + movie.getDirector());
            txtDescription.setText("- Description: " + movie.getDescription());

            player = new ExoPlayer.Builder(getActivity()).build();
            playerView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(movie.getUrlMovie());
            player.setMediaItem(mediaItem);
            player.prepare();
            player.setPlayWhenReady(true);

            Movie newMovie = new Movie();
            newMovie.setTitle(movie.getTitle());
            newMovie.setUrlImageMovie(movie.getUrlImageMovie());
            newMovie.setGenre(movie.getGenre());
            newMovie.setDirector(movie.getDirector());
            newMovie.setId(movie.getId());
            newMovie.setUserId(currentUser.getUid());

            firestore.collection("history")
                    .add(newMovie).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(getActivity(),
                            "Your add " + movie.getTitle() + " to History Successfull!",
                            Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),
                            "Your add " + movie.getTitle() + " to History Fail!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static DetailMovieFragment newInstance(Bundle bundle) {
        DetailMovieFragment fragment = new DetailMovieFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        bindingView();
        bindingAction();
        setupContentMovie();
    }

    @Override
    public void onStop() {
        super.onStop();
        player.setPlayWhenReady(false);
        player.release();
        player = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_detail_movie, container, false);
        return view;
    }
}