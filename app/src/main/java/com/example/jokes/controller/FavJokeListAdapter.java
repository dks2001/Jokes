package com.example.jokes.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jokes.R;
import com.example.jokes.model.Joke;
import com.example.jokes.view.FavJokeViewHolder;

import java.util.List;

public class FavJokeListAdapter extends RecyclerView.Adapter<FavJokeViewHolder> {

    private List<Joke> mjokeList;
    private Context mContext;

    public Context getContext() {
        return mContext;
    }

    public FavJokeListAdapter(List jokeList , Context context) {
        mjokeList = jokeList;
        mContext = context;
    }

    @NonNull
    @Override
    public FavJokeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_joke_item,parent,false);
        return new FavJokeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavJokeViewHolder holder, int position) {

        String jokeText = mjokeList.get(position).getJokeText();
        holder.getTxtFaveJoke().setText(jokeText);

        holder.getShareButtonfavListItem().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent  = new Intent(Intent.ACTION_SEND);
                String shareBody  = jokeText;
                intent.setType("text/plain");
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Mama Joke");
                intent.putExtra(android.content.Intent.EXTRA_TEXT,shareBody);
                mContext.startActivity(intent.createChooser(intent,"share via"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mjokeList.size();
    }
}
