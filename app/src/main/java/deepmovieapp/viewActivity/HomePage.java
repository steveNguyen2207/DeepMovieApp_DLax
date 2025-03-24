package com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vn.nguyenhoanganhtu.deepmovieapp.MainActivity;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment.ContentMovieFragment;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment.DetailMovieFragment;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment.FavourieFragment;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment.ProfileFragment;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment.SearchFragment;

public class HomePage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ImageView btnHome;
    private ImageView btnSearch;
    private ImageView btnFavourie;
    private ImageView btnProfile;
    private ContentMovieFragment contentFragment;
    private FavourieFragment favourieFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;

    void bindingView() {
        btnHome = findViewById(R.id.imageView);
        btnSearch = findViewById(R.id.imageView2);
        btnFavourie = findViewById(R.id.imageView5);
        btnProfile = findViewById(R.id.imageView6);
    }

    void bindingAction() {
        btnHome.setOnClickListener(this::onClickBtnHome);
        btnSearch.setOnClickListener(this::onClickBtnSearch);
        btnFavourie.setOnClickListener(this::onClickBtnFavourie);
        btnProfile.setOnClickListener(this::onClickBtnProfile);
    }

    private void onClickBtnProfile(View view) {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    private void onClickBtnFavourie(View view) {
        if (favourieFragment == null) {
            favourieFragment = new FavourieFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, favourieFragment)
                .addToBackStack(null)
                .commit();
    }

    private void onClickBtnSearch(View view) {
        if (searchFragment == null) {
            searchFragment = new SearchFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, searchFragment)
                .addToBackStack(null)
                .commit();
    }

    private void onClickBtnHome(View view) {
        if (contentFragment == null) {
            contentFragment = new ContentMovieFragment();
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainerView, contentFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAuth = FirebaseAuth.getInstance();
        bindingView();
        bindingAction();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }
}