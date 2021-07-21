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

public class ColorsFragment extends Fragment {
    private Context mContext;
    private ItemClickListener listener;

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
            is1 = mContext.getAssets().open("WordsList/colors.txt");
            is2 = mContext.getAssets().open("WordsList/translated_colors.txt");
            BufferedReader r1 = new BufferedReader(new InputStreamReader(is1));
            BufferedReader r2 = new BufferedReader(new InputStreamReader(is2));
            ArrayList<Word> words= new ArrayList<>(10);
            int[] idList={
                    R.drawable.color_black,
                    R.drawable.color_brown,
                    R.drawable.color_dusty_yellow,
                    R.drawable.color_gray,
                    R.drawable.color_green,
                    R.drawable.color_mustard_yellow,
                    R.drawable.color_red,
                    R.drawable.color_white
            };

            int[] media_id = {
                    R.raw.black,
                    R.raw.brown,
                    R.raw.dusty_yellow,
                    R.raw.gray,
                    R.raw.green,
                    R.raw.mustard_yellow,
                    R.raw.red,
                    R.raw.white
            };
            String line=r1.readLine();
            int k=0;
            while(line!=null){
                words.add(new Word(line,r2.readLine(),idList[k],media_id[k]));
                k++;
                line=r1.readLine();
            }
            WordAdapter colorsList=new WordAdapter(mContext,words,R.color.colors);
            ListView listView=view.findViewById(R.id.list);

            listView.setAdapter(colorsList);

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