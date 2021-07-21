package com.example.english_hinditranslator;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PhrasesFragment extends Fragment {

    private Context mContext;
    ItemClickListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.word_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputStream is1 = null;
        InputStream is2 = null;
        int[] media_id = {
                R.raw.phrase1,
                R.raw.phrase2,
                R.raw.phrase3,
                R.raw.phrase4,
                R.raw.phrase5,
                R.raw.phrase6,
                R.raw.phrase7,
                R.raw.phrase8,
                R.raw.phrase9,
                R.raw.phrase10,
                R.raw.phrase11,
                R.raw.phrase12,
                R.raw.phrase13,
                R.raw.phrase14,
                R.raw.phrase15,
                R.raw.phrase16,
                R.raw.phrase17,
                R.raw.phrase18
        };
        try {
            is1 = mContext.getAssets().open("WordsList/phrases.txt");
            is2 = mContext.getAssets().open("WordsList/translated_phrases.txt");
            BufferedReader r1 = new BufferedReader(new InputStreamReader(is1));
            BufferedReader r2 = new BufferedReader(new InputStreamReader(is2));
            ArrayList<Word> words=new ArrayList<>(10);
            String line=r1.readLine();
            int k=0;
            while(line!=null){
                words.add(new Word(line,r2.readLine(),media_id[k++]));
                line=r1.readLine();
            }
            WordAdapter phrasesList=new WordAdapter(mContext,words,R.color.phrases);
            ListView listView=view.findViewById(R.id.list);

            listView.setAdapter(phrasesList);

            listener = new ItemClickListener(mContext,words);
            listView.setOnItemClickListener(listener);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(is1!=null && is2!=null){
                try {
                    is1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    is2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        listener.releaseMediaPlayer(listener.v);
    }
}