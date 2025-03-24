package com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    private RecyclerView rcv_contentViewMovie;
    private ListMovieGenreAdapter listMovieGenreAdapter;
    List<ListMovieGenre> listMovieGenre = new ArrayList<>();
    List<Movie> listMovies = new ArrayList<>();
    private ImageView imgBack;

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    void bindingView() {
        rcv_contentViewMovie = findViewById(R.id.rcv_contentViewMovie);
        imgBack = findViewById(R.id.imgBack);
    }

    void bindingAction() {
        imgBack.setOnClickListener(this::onClickBtnBack);
    }

    private void onClickBtnBack(View view) {
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);
        finish();
    }
    void setupRecyclerView() {
        listMovieGenre.clear();

        listMovieGenreAdapter = new ListMovieGenreAdapter(getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager( getApplicationContext(),
                RecyclerView.VERTICAL,
                false);
        rcv_contentViewMovie.setLayoutManager(linearLayoutManager);
        listMovieGenreAdapter.setData(getListMovie());
        rcv_contentViewMovie.setAdapter(listMovieGenreAdapter);
    }

    private List<ListMovieGenre> getListMovie() {

        CheckApplication checkApplication = CheckApplication.getInstance();
        boolean check = checkApplication.isCheck();

        List<Movie> listMoviesTemp = new ArrayList<>();

        CollectionReference movieCollectionRef = firestore.collection("history");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        bindingView();
        setupRecyclerView();
        bindingAction();
    }
}