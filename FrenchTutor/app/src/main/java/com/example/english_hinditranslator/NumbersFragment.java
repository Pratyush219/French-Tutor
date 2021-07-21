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
import java.util.Objects;

public class NumbersFragment extends Fragment {
    ItemClickListener listener;
    ListView listView;
    ArrayList<Word> words;
    Context mContext;

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
        try {
            is1 = mContext.getAssets().open("WordsList/numbers_list.txt");
            is2 = mContext.getAssets().open("WordsList/translated_numbers.txt");
            BufferedReader r1 = new BufferedReader(new InputStreamReader(is1));
            BufferedReader r2 = new BufferedReader(new InputStreamReader(is2));
            words=new ArrayList<>(10);
            int[] idList={
                    R.drawable.number_one,
                    R.drawable.number_two,
                    R.drawable.number_three,
                    R.drawable.number_four,
                    R.drawable.number_five,
                    R.drawable.number_six,
                    R.drawable.number_seven,
                    R.drawable.number_eight,
                    R.drawable.number_nine,
                    R.drawable.number_ten
            };
            int[] media_id = {
                    R.raw.one,
                    R.raw.two,
                    R.raw.three,
                    R.raw.four,
                    R.raw.five,
                    R.raw.six,
                    R.raw.seven,
                    R.raw.eight,
                    R.raw.nine,
                    R.raw.ten
            };
            String line=r1.readLine();
            int k=0;
            while(line!=null){
                words.add(new Word(line, r2.readLine(),idList[k],media_id[k]));
                k++;
                line=r1.readLine();
            }
            WordAdapter numbersList=new WordAdapter(mContext,words,R.color.numbers);
            listView=view.findViewById(R.id.list);

            listView.setAdapter(numbersList);

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