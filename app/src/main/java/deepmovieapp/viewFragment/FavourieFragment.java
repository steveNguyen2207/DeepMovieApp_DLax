package com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.vn.nguyenhoanganhtu.deepmovieapp.MainActivity;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;
import com.vn.nguyenhoanganhtu.deepmovieapp.itemConfigAdapter.ListMovieGenreAdapter;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.ListMovieGenre;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.Movie;
import com.vn.nguyenhoanganhtu.deepmovieapp.utils.CheckApplication;
import com.vn.nguyenhoanganhtu.deepmovieapp.utils.DataCenter;

import java.util.ArrayList;
import java.util.List;

public class FavourieFragment extends Fragment {

    private View view;
    private RecyclerView rcv_contentViewMovie;
    private ListMovieGenreAdapter listMovieGenreAdapter;
    List<ListMovieGenre> listMovieGenre = new ArrayList<>();
    List<Movie> listMovies = new ArrayList<>();

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    void bindingView() {
        rcv_contentViewMovie = view.findViewById(R.id.rcv_contentViewMovie);
    }

    void bindingAction() {

    }

    void setupRecyclerView() {
        listMovieGenreAdapter = new ListMovieGenreAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( view.getContext(),
                RecyclerView.VERTICAL,
                false);
        rcv_contentViewMovie.setLayoutManager(linearLayoutManager);
        listMovieGenreAdapter.setData(getListMovie());
        rcv_contentViewMovie.setAdapter(listMovieGenreAdapter);
    }

    private List<ListMovieGenre> getListMovie() {

//        CheckApplication checkApplication = CheckApplication.getInstance();
//        boolean check = checkApplication.isCheck();

        listMovieGenre.clear();

        CollectionReference movieCollectionRef = firestore.collection("favourite");
        movieCollectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                // Xử lý dữ liệu khi lấy thành công
                for (DocumentSnapshot document : queryDocumentSnapshots) {
                    // Lấy thông tin từ DocumentSnapshot và thêm vào movieList
                    Movie movie = document.toObject(Movie.class);
                    if (movie.getUserId().contentEquals(currentUser.getUid())) {
                        if (!listMovies.contains(movie)) {
                            listMovies.add(movie);
                        }
                    }
                }
                listMovieGenre.add(new ListMovieGenre("", listMovies));
                listMovieGenreAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Xử lý khi lấy dữ liệu thất bại
            }
        });

        return listMovieGenre;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        bindingView();
        setupRecyclerView();
        bindingAction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_favourie, container, false);
        return view;
    }
}