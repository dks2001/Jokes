package com.example.jokes.view;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jokes.R;

public class FavJokeViewHolder extends RecyclerView.ViewHolder {


    private TextView txtFaveJoke;
    private ImageButton shareButtonfavListItem;



    public FavJokeViewHolder(@NonNull View itemView) {
        super(itemView);

        txtFaveJoke = itemView.findViewById(R.id.txtFaveJoke);
        shareButtonfavListItem = itemView.findViewById(R.id.shareButtonfavListItem);

    }

    public TextView getTxtFaveJoke() {
        return txtFaveJoke;
    }

    public ImageButton getShareButtonfavListItem() {
        return shareButtonfavListItem;
    }
}
