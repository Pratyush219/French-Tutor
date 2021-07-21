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

public class FamilyFragment extends Fragment {
    ItemClickListener listener;
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
            is1 = mContext.getAssets().open("WordsList/family_members.txt");
            is2 = mContext.getAssets().open("WordsList/translated_family_members.txt");
            BufferedReader r1 = new BufferedReader(new InputStreamReader(is1));
            BufferedReader r2 = new BufferedReader(new InputStreamReader(is2));
            ArrayList<Word> words= new ArrayList<>();
            int[] idList={
                    R.drawable.family_father,
                    R.drawable.family_mother,
                    R.drawable.family_son,
                    R.drawable.family_daughter,
                    R.drawable.family_grandfather,
                    R.drawable.family_grandmother,
                    R.drawable.family_older_brother,
                    R.drawable.family_older_sister,
                    R.drawable.family_younger_brother,
                    R.drawable.family_younger_sister
            };
            int[] media_id = {
                    R.raw.father,
                    R.raw.mother,
                    R.raw.son,
                    R.raw.daughter,
                    R.raw.grandfather,
                    R.raw.grandmother,
                    R.raw.elder_brother,
                    R.raw.elder_sister,
                    R.raw.younger_brother,
                    R.raw.younger_sister
            };
            String line=r1.readLine();
            int k=0;
            while(line!=null){
                words.add(new Word(line,r2.readLine(),idList[k],media_id[k]));
                k++;
                line=r1.readLine();
            }
            WordAdapter familyList=new WordAdapter(mContext,words,R.color.family_members);
            ListView listView=view.findViewById(R.id.list);

            listView.setAdapter(familyList);

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