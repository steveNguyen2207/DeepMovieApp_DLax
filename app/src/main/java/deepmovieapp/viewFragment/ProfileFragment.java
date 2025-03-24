package com.vn.nguyenhoanganhtu.deepmovieapp.viewFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.User;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity.History;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity.HomePage;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity.Login;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity.Setting;
import com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity.TrendingFilm;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {
    private View view;
    private Button btnLogout;
    private Button btnHistory;
    private Button btnFavorie;
    private Button btnSetting;
    private Button btnChatWithAI;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private ImageView imgProfile;
    private TextView txtName;
    private FavourieFragment favourieFragment;

    void bindingView() {
        btnLogout = view.findViewById(R.id.btnLogout);
        btnHistory = view.findViewById(R.id.btnHistory);
        btnChatWithAI = view.findViewById(R.id.btnChatWithAI);
        btnSetting = view.findViewById(R.id.btnSetting);
        btnFavorie = view.findViewById(R.id.btnFavorie);
        imgProfile = view.findViewById(R.id.imgProfile);
        txtName = view.findViewById(R.id.txtName);
    }

    void bindingAction() {
        btnLogout.setOnClickListener(this::onClickBtnLogout);
        btnSetting.setOnClickListener(this::onClickBtnSetting);
        btnHistory.setOnClickListener(this::onClickBtnHis);
        btnFavorie.setOnClickListener(this::onClickBtnFavourie);
        btnChatWithAI.setOnClickListener(this::onClickChatAI);
    }

    private void onClickBtnFavourie(View view) {
//        if (favourieFragment == null) {
//            favourieFragment = new FavourieFragment();
//        }
//        getChildFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragmentContainerView, favourieFragment)
//                .addToBackStack(null)
//                .commit();
        Intent intent = new Intent(getActivity(), HomePage.class);
        startActivity(intent);
    }

    private void onClickChatAI(View view) {
        Intent intent = new Intent(getActivity(), TrendingFilm.class);
        startActivity(intent);
    }

    private void onClickBtnHis(View view) {
        Intent intent = new Intent(getActivity(), History.class);
        startActivity(intent);
    }

    private void onClickBtnSetting(View view) {
        Intent intent = new Intent(getContext(), Setting.class);
        startActivity(intent);
    }

    private void onClickBtnLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
    }

    void setupProfile() {
        CollectionReference collectionRef = firestore.collection("user");
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> userList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        userList.add(user);
                    }
                    for (User user : userList) {
                        Log.w("sy.nguyenvan", user.getUserId() + "---" + currentUser.getUid());
                        if (user.getUserId().contentEquals(currentUser.getUid())) {
                            String imageUrl = user.getProfile();
                            Picasso.get().load(imageUrl).into(imgProfile);
                            txtName.setText(user.getName());
                        }
                    }
                } else {
                    Exception e = task.getException();
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindingView();
        bindingAction();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        setupProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }
}