package com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;

public class Register extends AppCompatActivity {
    private Button btnRegister;
    private TextInputEditText edtEmailValue;
    private TextInputEditText edtPasswordValue;
    private TextInputEditText edtCFPasswordValue;
    private TextView edtLogin;
    private ProgressBar prgLoading;
    private FirebaseAuth mAuth;
    private ImageView imgBackLogin;

    void bindingView() {
        btnRegister = findViewById(R.id.btnForgot);
        edtEmailValue = findViewById(R.id.edtEmailValue);
        edtPasswordValue = findViewById(R.id.edtPasswordValue);
        edtCFPasswordValue = findViewById(R.id.edtCFPasswordValue);
        prgLoading = findViewById(R.id.prgLoadingg);
        edtLogin = findViewById(R.id.edtLogin);
        imgBackLogin = findViewById(R.id.imgBackLogin);
    }

    void bindingAction() {
        btnRegister.setOnClickListener(this::onClickBtnRegister);
        edtLogin.setOnClickListener(this::onClickBtnLogin);
        imgBackLogin.setOnClickListener(this::onClickBtnBack);
    }

    private void onClickBtnBack(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    private void onClickBtnLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }

    private void onClickBtnRegister(View view) {
        prgLoading.setVisibility(View.VISIBLE);
        String email = String.valueOf(edtEmailValue.getText());
        String pass = String.valueOf(edtPasswordValue.getText());
        String passcf = String.valueOf(edtCFPasswordValue.getText());

        if (email.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Email IsEmpty", Toast.LENGTH_SHORT).show();
            prgLoading.setVisibility(View.GONE);
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Password IsEmpty", Toast.LENGTH_SHORT).show();
            prgLoading.setVisibility(View.GONE);
            return;
        }
        if (!pass.contentEquals(passcf)) {
            Toast.makeText(getApplicationContext(), "Confirm Password Is Not Macth", Toast.LENGTH_SHORT).show();
            prgLoading.setVisibility(View.GONE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        prgLoading.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Account Create Succesfull!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.w("sy.nguyenvan", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Account Create Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        bindingView();
        bindingAction();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
            finish();
        }
    }
}