package com.example.minitwitter.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.data.TweetViewModel;

public class BottomModalTweetFragment extends BottomSheetDialogFragment {

    private TweetViewModel tweetViewModel;
    private int idTweetDelete;
    private int posTweetDelete;

    public static BottomModalTweetFragment newInstance(int idTweet, int posTweet) {
        BottomModalTweetFragment fragment = new BottomModalTweetFragment();
        Bundle args = new Bundle();
        args.putInt(Constantes.ARG_TWEET_ID, idTweet);
        args.putInt(Constantes.ARG_TWEET_POS, posTweet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            idTweetDelete = getArguments().getInt(Constantes.ARG_TWEET_ID);
            posTweetDelete = getArguments().getInt(Constantes.ARG_TWEET_POS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_modal_tweet_fragment, container, false);

        final NavigationView nav = v.findViewById(R.id.navigation_view_bottom_tweet);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == R.id.action_delete_tweet){
                    tweetViewModel.deleteTweet(idTweetDelete, posTweetDelete);
                    getDialog().dismiss();
                    return true;
                }
                return false;
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tweetViewModel = ViewModelProviders.of(getActivity()).get(TweetViewModel.class);
    }

}
