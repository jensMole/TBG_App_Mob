package com.example.jensie.tbg;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by jensie on 16-11-2017.
 * We gaan er hier voor zorgen dat men een account kan aanmaken op de app.
 */

public class SignupActivity extends AppCompatActivity{

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        //als je op de reset knop drukt
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//resetpasswoord scherm tonen.
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        //als je op de sign in knop drukt ga je dit scherm afsluiten.
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //als je op de signup knop drukt.
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                //connectivity opzetten
                ConnectivityManager cManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                //bekijken als men verbonden is.
                NetworkInfo nInfo = cManager.getActiveNetworkInfo();
                //kijken als je verbonden bent of niet en toon een toast.
                if(nInfo != null && nInfo.isConnected()) {

                    //kijken als je de velden hebt ingevuld.
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplicationContext(), "Vul email adres in!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(getApplicationContext(), "Vul wachtwoord in!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //als het wachtwoord zeker langer is dan 5 tekens.
                    if (password.length() < 6) {
                        Toast.makeText(getApplicationContext(), "Wachtwoord is te kort, vul minstens 6 karakters in!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    //create user
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    // If sign in fails, display a message to the user. If sign in succeeds
                                    // the auth state listener will be notified and logic to handle the
                                    // signed in user can be handled in the listener.
                                    if (!task.isSuccessful()) {//Als het niet werkte.
                                        Toast.makeText(SignupActivity.this, "Authenticatie gefaald." + task.getException(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {//Als het werkte
                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                }else{//Als je geen internet hebt.
                    Toast.makeText(getApplicationContext(), "Deze applicatie vraagt wifi of 4G, gelieve deze aan te zetten.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
