package me.doapps.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.doapps.adapters.Music_Adapter;
import me.doapps.beans.Music_DTO;
import me.doapps.descargarmp3.Main;
import me.doapps.descargarmp3.R;
import me.doapps.tasks.Task_Music;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Fragment_Search extends Fragment {

    private ListView listatracks;
    private EditText editText;

    private ProgressDialog progressDialog;
    protected Music_Adapter music_adapter;

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

        listatracks = (ListView) getView().findViewById(R.id.list_music);
        listatracks.setOnItemClickListener(onItemClickListener);

        editText = (EditText) getView().findViewById(R.id.txt_search);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    buscarTracks();
                    return true;
                }
                return false;
            }
        });

        /**
         * EMPTY - SHOW
         */
        listatracks.setVisibility(View.GONE);
        getView().findViewById(R.id.frameempty).setVisibility(View.VISIBLE);


        getView().findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarTracks();
            }
        });
    }

    /**
     * Hides the soft keyboard
     */
    public void hideSoftKeyboard() {
        if(getActivity().getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void buscarTracks(){
        if (!(editText.getText().toString().matches(""))) {
            getView().findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressDialog = ProgressDialog.show(getActivity(), null, "Buscando...!", true);
                    Task_Music task_music = new Task_Music(getActivity());
                    task_music.sendRequestMusics(editText.getText().toString());
                    task_music.setInterface_music(new Task_Music.Interface_Music() {
                        @Override
                        public void getMusic(boolean status, ArrayList<Music_DTO> music_dtos) {
                            listatracks.setVisibility(View.VISIBLE);
                            getView().findViewById(R.id.frameempty).setVisibility(View.GONE);
                            progressDialog.hide();
                            if (status) {
                                hideSoftKeyboard();
                                music_adapter = new Music_Adapter(music_dtos, getActivity());
                                listatracks.setAdapter(music_adapter);
                            }
                        }
                    });
                }
            });
        } else {
            Toast.makeText(getActivity(), "Ingrese un criterio de busqueda...!", Toast.LENGTH_SHORT).show();
        }
    }

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Music_DTO music_dto = (Music_DTO) parent.getItemAtPosition(position);
            getActivity().getIntent().putExtra("music_dto",music_dto);
            ((Main)getActivity()).mPager.setCurrentItem(2);
        }
    };
}
