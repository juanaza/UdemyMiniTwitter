package com.example.minitwitter;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.response.Like;
import com.example.minitwitter.retrofit.response.Tweet;

import org.w3c.dom.Text;

import java.util.List;

public class MyTweetRecyclerViewAdapter extends RecyclerView.Adapter<MyTweetRecyclerViewAdapter.ViewHolder> {
    private Context ctx;
    private List<Tweet> mValues;
    private String username;

    public MyTweetRecyclerViewAdapter(Context context, List<Tweet> items) {
        mValues = items;
        ctx = context;
        username = SharedPreferencesManager.getStringValue(Constantes.PREF_USERNAME);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.textViewUsername.setText(holder.mItem.getUser().getUsername());
        holder.textViewMessage.setText(holder.mItem.getMensaje());
        holder.textViewLikesCount.setText(holder.mItem.getLikes().size());

        String photo = holder.mItem.getUser().getPhotoUrl();
        if(photo.equals("")){
            Glide.with(ctx)
                    .load("https://www.minitwitter.com/apiv1/uploads/photos/" + photo)
                    .into(holder.imageViewavatar);
        }

        for(Like like: holder.mItem.getLikes()){
            if(like.getUsername().equals(username)){
                Glide.with(ctx)
                        .load(R.drawable.ic_like_pink)
                        .into(holder.imageViewLike);
                holder.textViewLikesCount.setTextColor(ctx.getResources().getColor(R.color.pink));
                holder.textViewLikesCount.setTypeface(null, Typeface.BOLD);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageViewavatar;
        public final ImageView imageViewLike;
        public final TextView textViewUsername;
        public final TextView textViewMessage;
        public final TextView textViewLikesCount;
        public Tweet mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageViewavatar = view.findViewById(R.id.imageViewAvatar);
            imageViewLike = view.findViewById(R.id.imageViewLike);
            textViewUsername = view.findViewById(R.id.textViewUsername);
            textViewMessage = view.findViewById(R.id.textViewMessage);
            textViewLikesCount = view.findViewById(R.id.textViewLikes);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewUsername.getText() + "'";
        }
    }
}
