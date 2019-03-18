package com.example.minitwitter.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.minitwitter.retrofit.response.Tweet;

import java.util.List;

public class TweetViewModel extends AndroidViewModel {
    private TweetRepository tweetRepository;
    private LiveData<List<Tweet>> tweets;
    private LiveData<List<Tweet>> favTweets;

    public TweetViewModel(@NonNull Application application) {
        super(application);
        tweetRepository = new TweetRepository();
        tweets = tweetRepository.getAllTweets();
    }

    public LiveData<List<Tweet>> getTweets(){
        return tweets;
    }

    public LiveData<List<Tweet>> getNewTweets(){
        tweets = tweetRepository.getAllTweets();
        return tweets;
    }

    public void insertTweet(String mensaje){
        tweetRepository.createTweet(mensaje);
    }

    public void likeTweet(int idTweet, int posiiton){
        tweetRepository.likeTweet(idTweet, posiiton);
    }

    public LiveData<List<Tweet>> getFavTweets() {
        favTweets = tweetRepository.getAllFavTweets();
        return favTweets;
    }

    public LiveData<List<Tweet>> getNewFavTweets(){
        getNewTweets();
        return getFavTweets();
    }
}
