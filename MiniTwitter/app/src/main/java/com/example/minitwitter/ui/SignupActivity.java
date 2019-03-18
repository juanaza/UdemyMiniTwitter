package com.example.minitwitter.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.MiniTwitterClient;
import com.example.minitwitter.retrofit.MiniTwitterService;
import com.example.minitwitter.retrofit.request.RequestSignup;
import com.example.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;
    private EditText editTextUsername, editTextEmail, editTextPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        retrofitInit();
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    private void retrofitInit(){
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    public void btnSignup_OnClick(View view){
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if(username.isEmpty()){
            editTextUsername.setError("El nombre de usuario es requerido");
        }
        else if(email.isEmpty()){
            editTextEmail.setError("El email es requerido");
        }
        else if(password.isEmpty() || password.length()<6){
            editTextPassword.setError("La contraseña es requerida y debe tener al menos 6 caracteres");
        }
        else{
            RequestSignup requestSignup = new RequestSignup(username, email, password, "UDEMYANDROID");
            Call<ResponseAuth> call = miniTwitterService.doSignup(requestSignup);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(SignupActivity.this, "Registro completado correctamente", Toast.LENGTH_SHORT).show();
                        SharedPreferencesManager.setStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setBooleanValue(Constantes.PREF_ACTIVE, response.body().getActive());

                        Intent i = new Intent(SignupActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Toast.makeText(SignupActivity.this, "Algo fue mal, revise sus datos de acceso", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignupActivity.this, "Problemas de conexión. Intentelo de nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void textViewGoLogin_OnClick(View view){
        Intent i = new Intent(SignupActivity.this, MainActivity.class);
        startActivity(i);
    }
}
