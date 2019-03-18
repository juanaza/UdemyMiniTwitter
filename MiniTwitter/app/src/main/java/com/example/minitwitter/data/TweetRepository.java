package com.example.minitwitter.data;

import android.arch.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.MyApp;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.AuthTwitterService;
import com.example.minitwitter.retrofit.request.RequestCreateTweet;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.Tweet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {
    private AuthTwitterService authTwitterService;
    private AuthTwitterClient authTwitterClient;
    private MutableLiveData<List<Tweet>> allTweets;
    private MutableLiveData<List<Tweet>> allFavTweets;
    String username;

    public TweetRepository(){
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
        username = SharedPreferencesManager.getStringValue(Constantes.PREF_USERNAME);
    }

    public MutableLiveData<List<Tweet>> getAllTweets(){
        if(allTweets == null){
            allTweets = new MutableLiveData<>();
        }
        Call<List<Tweet>> call = authTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()){
                    allTweets.setValue(response.body());
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
        return allTweets;
    }

    public MutableLiveData<List<Tweet>> getAllFavTweets() {
        if(allFavTweets == null){
            allFavTweets = new MutableLiveData<>();
        }
        List<Tweet> newFavList = new ArrayList<>();
        Iterator itTweets = allTweets.getValue().iterator();
        while(itTweets.hasNext()){
            Tweet current = (Tweet) itTweets.next();
            Iterator itLikes = current.getLikes().iterator();
            boolean enc = false;
            while(itLikes.hasNext() && !enc){
                Like like = (Like) itLikes.next();
                if(like.getUsername().equals(username)){
                    enc = true;
                    newFavList.add(current);
                }
            }
        }
        allFavTweets.setValue(newFavList);
        return allFavTweets;
    }

    public void createTweet(String mensaje){
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(mensaje);
        Call<Tweet> call = authTwitterService.createTweet(requestCreateTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> listaClonada = new ArrayList<>();
                    listaClonada.add(response.body());
                    listaClonada.addAll(allTweets.getValue());

                    allTweets.setValue(listaClonada);
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void likeTweet(int idTweet, final int position){
        Call<Tweet> call = authTwitterService.likeTweet(idTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> listaClonada = new ArrayList<>();
                    listaClonada.addAll(allTweets.getValue());
                    listaClonada.set(position, response.body());

                    allTweets.setValue(listaClonada);
                    getAllFavTweets();
                }
                else{
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal, inténtelo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
