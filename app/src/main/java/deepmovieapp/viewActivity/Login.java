package com.vn.nguyenhoanganhtu.deepmovieapp.viewActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vn.nguyenhoanganhtu.deepmovieapp.MainActivity;
import com.vn.nguyenhoanganhtu.deepmovieapp.R;
import com.vn.nguyenhoanganhtu.deepmovieapp.models.User;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    private Button btnLogin;
    private Button btnLoginGG;
    private TextInputEditText edtEmailValue;
    private TextInputEditText edtPasswordValue;
    private TextView edtForgotPassword;
    private TextView edtRegister;
    private ProgressBar prgLoading;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private ImageView imgBack;
    private GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog progressDialog;
    private static int RC_SIGN_IN = 40;

    void bindingView() {
        btnLogin = findViewById(R.id.btnLogin);
        btnLoginGG = findViewById(R.id.btnLoginGG);
        edtEmailValue = findViewById(R.id.edtEmailValue);
        edtPasswordValue = findViewById(R.id.edtPasswordValue);
        edtForgotPassword = findViewById(R.id.edtForgotPassword);
        prgLoading = findViewById(R.id.prgLoading);
        edtRegister = findViewById(R.id.edtLogin);
        imgBack = findViewById(R.id.imgBack);
    }

    void bindingAction() {
        btnLogin.setOnClickListener(this::onClickBtnLogin);
        btnLoginGG.setOnClickListener(this::onClickBtnLoginGG);
        edtRegister.setOnClickListener(this::onClickBtnRegister);
        imgBack.setOnClickListener(this::onClickBtnBack);
        edtForgotPassword.setOnClickListener(this::onClickBtnForgot);
    }

    private void onClickBtnForgot(View view) {
        Intent intent = new Intent(getApplicationContext(), ForgotPassword.class);
        startActivity(intent);
        finish();
    }

    private void onClickBtnBack(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void onClickBtnRegister(View view) {
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
        finish();
    }

    private void onClickBtnLoginGG(View view) {
        prgLoading.setVisibility(View.VISIBLE);
        if(currentUser == null){
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, RC_SIGN_IN);
        }
    }

    private void onClickBtnLogin(View view) {
        prgLoading.setVisibility(View.VISIBLE);
        String email = String.valueOf(edtEmailValue.getText());
        String pass = String.valueOf(edtPasswordValue.getText());

        if (email.isEmpty()) {
            Toast.makeText(Login.this, "Email IsEmpty", Toast.LENGTH_SHORT).show();
            prgLoading.setVisibility(View.GONE);
            return;
        }
        if (pass.isEmpty()) {
            Toast.makeText(Login.this, "Password IsEmpty", Toast.LENGTH_SHORT).show();
            prgLoading.setVisibility(View.GONE);
            return;
        }

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        prgLoading.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Login Succesfull!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomePage.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("sy.nguyenvan", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Login Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    void setUpDialog() {
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We are creating your account");
    }

    void configGGSignOption() {
        GoogleSignInOptions ggOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), ggOptions);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =  GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthe(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseAuthe(String idToken) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            User users = new User();
                            users.setUserId(user.getUid());
                            users.setName(user.getDisplayName());
                            users.setProfile(user.getPhotoUrl().toString());

                            firestore.collection("user")
                                    .add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Toast.makeText(getApplicationContext(),
                                            "Login with google successfull!",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), HomePage.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),
                                            "Login with google fail!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Log.w("sy.nguyenvan", "signInWithGoogle:failure", task.getException());
                            Toast.makeText(Login.this, "Login Fail!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        bindingView();
        bindingAction();
        setUpDialog();
        configGGSignOption();
    }
}