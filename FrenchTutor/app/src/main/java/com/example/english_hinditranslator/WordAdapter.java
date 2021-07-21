package com.example.english_hinditranslator;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    public int color;
    public WordAdapter(Context context, ArrayList<Word> objects, int color) {
        super(context, 0, objects);
        this.color = color;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Word currentWord = getItem(position);
        ImageView imageView = listItemView.findViewById(R.id.temp);
        if(currentWord.getImageResourceId() != currentWord.NO_IMAGE_VIEW) {
            imageView.setImageResource(currentWord.getImageResourceId());
        }
        else{
            imageView.setVisibility(View.GONE);
        }
        listItemView.setBackgroundColor(ContextCompat.getColor(getContext(),color));
        LinearLayout messageContainer = listItemView.findViewById(R.id.container);
        messageContainer.setBackgroundColor(ContextCompat.getColor(getContext(),color));

        TextView hindiTranslation = listItemView.findViewById(R.id.hindi);
        hindiTranslation.setText(currentWord.getFrenchTranslation());

        TextView defaultTranslation = listItemView.findViewById(R.id.eng);
        defaultTranslation.setText(currentWord.getdefaultTranslation());

        ImageView btn_play = listItemView.findViewById(R.id.play);
        ImageView btn_pause = listItemView.findViewById(R.id.pause);
        btn_play.setBackgroundColor(ContextCompat.getColor(getContext(),color));
        btn_pause.setBackgroundColor(ContextCompat.getColor(getContext(),color));

//        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                btn_pause.setVisibility(View.GONE);
//                btn_play.setVisibility(View.VISIBLE);
//            }
//        });
//        mediaPlayer.release();

        return listItemView;
    }


}
