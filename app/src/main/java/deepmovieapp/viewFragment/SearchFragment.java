package com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;
import com.vn.nguyenhoanganhtu.deepmovieapp.itemConfigAdapter.ListMovieGenreAdapter;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.ListMovieGenre;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.Movie;
import com.vn.nguyenhoanganhtu.deepmovieapp.utils.DataCenter;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private EditText txtSearch;
    private View view;
    private ImageView btnSearchFilm;
    private RecyclerView rcv_contentViewMovieSearch;
    private ListMovieGenreAdapter listMovieGenreAdapter;
    List<ListMovieGenre> listMovieGenre = new ArrayList<>();
    List<Movie> listMovies = new ArrayList<>();
    void bindingView() {
     txtSearch = view.findViewById(R.id.textNameFilmValue);
     btnSearchFilm= view.findViewById(R.id.btnSearchFilm);
     rcv_contentViewMovieSearch = view.findViewById(R.id.rcv_contentViewMovieSearch);
    }

    void bindingAction() {
        btnSearchFilm.setOnClickListener(this::onClickBtnSearch);
    }

    private void onClickBtnSearch(View view) {
        setupRecyclerView();
    }

    void setupRecyclerView() {

        DataCenter dataCenter = DataCenter.getInstance();
        List<Movie> movies = dataCenter.getListMovie();

        listMovieGenre.clear();
        List<Movie> listMovies = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getTitle().toLowerCase().contains(txtSearch.getText())) {
                listMovies.add(movie);
            }
        }
        listMovieGenre.add(new ListMovieGenre("", listMovies));
        listMovieGenreAdapter = new ListMovieGenreAdapter(view.getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( view.getContext(),
                RecyclerView.VERTICAL,
                false);
        rcv_contentViewMovieSearch.setLayoutManager(linearLayoutManager);
        listMovieGenreAdapter.setData(listMovieGenre);
        rcv_contentViewMovieSearch.setAdapter(listMovieGenreAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView();
        bindingAction();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }
}