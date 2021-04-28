package com.example.jokes.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jokes.R;
import com.example.jokes.controller.FavJokeListAdapter;
import com.example.jokes.model.Joke;
import com.example.jokes.model.JokeManager;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;


public class FavJokeFragment extends Fragment {

    RecyclerView recyclerView;
    JokeManager jokeManager;
    FavJokeListAdapter favJokeListAdapter;
    private List<Joke> jokeList = new ArrayList<>();

    private Joke deleteJoke;


    public FavJokeFragment() {
        // Required empty public constructor
    }


    public static FavJokeFragment newInstance() {
        FavJokeFragment fragment = new FavJokeFragment();
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        jokeManager = new JokeManager(context);
        jokeList.clear();
        if(jokeManager.retrieveJoke().size()>0) {
            for(Joke joke : jokeManager.retrieveJoke()) {
                jokeList.add(joke);
            }
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fav_joke, container, false);
        if(view != null) {
            recyclerView = view.findViewById(R.id.rv);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            favJokeListAdapter = new FavJokeListAdapter(jokeList,getContext());
            recyclerView.setAdapter(favJokeListAdapter);

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();
            switch (direction) {

                case ItemTouchHelper.LEFT:
                case ItemTouchHelper.RIGHT:

                    deleteJoke = jokeList.get(position);
                    jokeManager.deleteJoke(jokeList.get(position));
                    jokeList.remove(position);
                    favJokeListAdapter.notifyDataSetChanged();
                    favJokeListAdapter.notifyItemRemoved(position);

                    Snackbar.make(recyclerView,"Joke is removed",Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    jokeList.add(position,deleteJoke);
                                    jokeManager.saveJoke(deleteJoke);
                                    favJokeListAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
            }
        }
    };
}