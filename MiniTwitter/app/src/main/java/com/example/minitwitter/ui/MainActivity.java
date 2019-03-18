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
import com.example.minitwitter.retrofit.request.RequestLogin;
import com.example.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private MiniTwitterClient miniTwitterClient;
    private MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        retrofitInit();
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
    }

    private void retrofitInit(){
        miniTwitterClient = MiniTwitterClient.getInstance();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    public void buttonLogin_OnClick(View view){
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if(email.isEmpty()){
            editTextEmail.setError("El email es requerido");
        }else if(password.isEmpty()){
            editTextPassword.setError("La contraseña es requerida");
        }else{
            RequestLogin requestLogin = new RequestLogin(email, password);
            Call<ResponseAuth> call = miniTwitterService.doLogin(requestLogin);
            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){
                        Toast.makeText(MainActivity.this, "Sesión iniciada correctamente", Toast.LENGTH_SHORT).show();
                        SharedPreferencesManager.setStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setStringValue(Constantes.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setBooleanValue(Constantes.PREF_ACTIVE, response.body().getActive());

                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Algo fue mal, revise sus datos de acceso", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Problemas de conexión. Intentelo de nuevo", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void textViewGoSignup_OnClick(View view){
        Intent i = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(i);
        finish();
    }
}
