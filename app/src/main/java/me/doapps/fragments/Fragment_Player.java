package me.doapps.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.io.IOException;

import me.doapps.adapters.Fragment_Adapter;
import me.doapps.beans.Music_DTO;
import me.doapps.descargarmp3.R;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Fragment_Player extends Fragment implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnBufferingUpdateListener {

    private MediaPlayer mp = null;
    private boolean isPlaying = false;
    private ImageView btn_play;
    private Music_DTO music_dto;

    public static final Fragment_Player newInstance(Music_DTO music_dto) {
        Fragment_Player fragment_player = new Fragment_Player();
        Bundle bundle = new Bundle();
        bundle.putSerializable("music_dto", music_dto);
        fragment_player.setArguments(bundle);
        return fragment_player;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        music_dto = (Music_DTO) getActivity().getIntent().getSerializableExtra("music_dto");
        ((TextView)getView().findViewById(R.id.nombremusica)).setText(music_dto.getName());

        btn_play = (ImageView)getView().findViewById(R.id.play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(isPlaying){
                    pause();
                }else{
                    play();
                }
            }
        });
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int i2) {
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                break;
            default:
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(music_dto != null){
            play();
        }
    }

    private void play() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                getView().findViewById(R.id.progressBar2).setVisibility(View.VISIBLE);
            }

            @Override
            protected Void doInBackground(Void... params) {
                if(music_dto.getUrl() != null){
                    Uri myUri = Uri.parse(music_dto.getUrl());
                    try {
                        if (mp == null) {
                            mp = new MediaPlayer();
                        } else {
                            mp.stop();
                            mp.reset();
                        }
                        mp.setDataSource(getActivity(), myUri); // Go to Initialized state
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mp.setOnPreparedListener(Fragment_Player.this);
                        mp.setOnBufferingUpdateListener(Fragment_Player.this);

                        mp.setOnErrorListener(Fragment_Player.this);
                        mp.prepareAsync();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                btn_play.setImageResource(android.R.drawable.ic_media_pause);
                isPlaying = true;
            }
        }.execute();
    }


    private void pause() {

        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;

            btn_play.setImageResource(android.R.drawable.ic_media_play);
            isPlaying = false;
            getView().findViewById(R.id.progressBar2).setVisibility(View.GONE);
        }
    }

}
