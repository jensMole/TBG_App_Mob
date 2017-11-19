package com.example.jensie.tbg;

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
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by jensie on 16-11-2017.
 * Als een gebruiker zijn wachtwoord wil resetten.
 */

public class ResetPasswordActivity extends AppCompatActivity{

    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //firebase binnen krijgen
        auth = FirebaseAuth.getInstance();

        //als je op de terug knop drukt
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//dit scherm afsluiten.
                finish();
            }
        });

        //als je op de reset knop drukt.
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();

                //connectivity opzetten
                ConnectivityManager cManager = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
                //bekijken als men verbonden is.
                NetworkInfo nInfo = cManager.getActiveNetworkInfo();
                //kijken als je verbonden bent of niet en toon een toast.
                if(nInfo != null && nInfo.isConnected()) {

                    //als je het mail veld leeg laat.
                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(getApplication(), "Vul je geregistreerde email in!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //progressbar tonen.
                    progressBar.setVisibility(View.VISIBLE);
                    //zenden van de email.
                    auth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {//Als het gelukt is
                                        Toast.makeText(ResetPasswordActivity.this, "We hebben je een mail gestuurd met instructies om je wachtwoord te resetten! Let op de mail kan in Spam staan.", Toast.LENGTH_LONG).show();
                                    } else {//als er iets niet klopt.
                                        Toast.makeText(ResetPasswordActivity.this, "Gefaald voor het zenden van de reset mail!", Toast.LENGTH_SHORT).show();
                                    }
                                    //progressbar weghalen.
                                    progressBar.setVisibility(View.GONE);
                                }
                            });
                }else{//Als je geen internet verbinding hebt.
                    Toast.makeText(getApplicationContext(), "Deze applicatie vraagt wifi of 4G, gelieve deze aan te zetten.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
