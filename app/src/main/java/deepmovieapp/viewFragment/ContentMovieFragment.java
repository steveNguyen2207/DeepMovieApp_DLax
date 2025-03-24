package com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;
import com.vn.nguyenhoanganhtu.deepmovieapp.itemConfigAdapter.ListMovieGenreAdapter;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.ListMovieGenre;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.Movie;
import com.vn.nguyenhoanganhtu.deepmovieapp.utils.DataCenter;
import com.vn.nguyenhoanganhtu.deepmovieapp.utils.DataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ContentMovieFragment extends Fragment {
    private View view;
    private RecyclerView rcv_contentViewMovie;
    private ListMovieGenreAdapter listMovieGenreAdapter;
    private StyledPlayerView playerView;
    private ExoPlayer player;

    List<ListMovieGenre> listMovieGenre = new ArrayList<>();
    List<Movie> listMovies = new ArrayList<>();

    Disposable disposable;

    @Override
    public void onResume() {
        super.onResume();
        disposable = listMovieGenreAdapter.getObjectObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object -> {
                    Log.w("sy.nguyenvan", "Có data rồi nè " + object.getTitle());
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("movie", object);
                    DetailMovieFragment fragmentB = DetailMovieFragment.newInstance(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragmentB);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                });
    }
    @Override
    public void onPause() {
        super.onPause();
        disposable.dispose();
    }
    void bindingView() {
        rcv_contentViewMovie = view.findViewById(R.id.rcv_contentViewMovie);
        playerView = view.findViewById(R.id.player_view);
    }

    void bindingAction() {

    }

    void setupRecyclerView() {
        listMovieGenreAdapter = new ListMovieGenreAdapter(view.getContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( view.getContext(),
                RecyclerView.VERTICAL,
                false);
        rcv_contentViewMovie.setLayoutManager(linearLayoutManager);
        listMovieGenreAdapter.setData(getListMovieForGenre());
        rcv_contentViewMovie.setAdapter(listMovieGenreAdapter);
    }

    private List<ListMovieGenre> getListMovieForGenre() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference movieCollectionRef = db.collection("movies");

        movieCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Xử lý dữ liệu khi lấy thành công
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    // Lấy thông tin từ DocumentSnapshot và thêm vào movieList
                    Movie movie = document.toObject(Movie.class);
                    listMovies.add(movie);
                    listMovieGenre.add(new ListMovieGenre(movie.getGenre(), listMovies));
                }
                listMovieGenreAdapter.notifyDataSetChanged();
                setupViewPlayMovie();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi lấy dữ liệu thất bại
            }
        });

        return listMovieGenre;
    }

    void setupViewPlayMovie() {
        player = new ExoPlayer.Builder(getActivity()).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(listMovies.get(0).getUrlMovie());
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView();
        setupRecyclerView();
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
        view = inflater.inflate(R.layout.fragment_content_movie, container, false);
        return view;
    }
}