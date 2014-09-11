package me.doapps.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import me.doapps.adapters.Music_Adapter;
import me.doapps.beans.Music_DTO;
import me.doapps.descargarmp3.R;
import me.doapps.tasks.Task_Music;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Fragment_Search extends Fragment {
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Button)getView().findViewById(R.id.btn_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(getActivity(), null, "Wait please...", true);

                String name = ((EditText)getView().findViewById(R.id.txt_search)).getText().toString();
                Task_Music task_music = new Task_Music(getActivity());
                task_music.sendRequestMusics(name);
                task_music.setInterface_music(new Task_Music.Interface_Music() {
                    @Override
                    public void getMusic(boolean status, ArrayList<Music_DTO> music_dtos) {
                        progressDialog.hide();
                        if(status){
                            ((ListView)getView().findViewById(R.id.list_music)).setAdapter(new Music_Adapter(music_dtos, getActivity()));
                        }
                        else{
                            Log.e("result", "vacio");
                        }
                    }
                });
            }
        });

    }
}
