package me.doapps.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.widget.ImageButton;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.io.IOException;

import me.doapps.adapters.Fragment_Adapter;
import me.doapps.descargarmp3.R;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Fragment_Player extends Fragment implements View.OnClickListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
    private ImageButton btn_play;

    private boolean playPause;
    private MediaPlayer player;

    private boolean initialStage = true;

    public static final Fragment_Player newInstance(String music_url){
        Fragment_Player fragment_player = new Fragment_Player();
        Bundle bundle = new Bundle();
        bundle.putSerializable("music_url", music_url);
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

        btn_play = (ImageButton) getView().findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.release();
        player = null;
    }




    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        return false;
    }

    @Override
    public void onClick(View view) {
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource("http://live6.goear.com/listen/f78dab236dfcd70c0fa3f49d0fca33ba/54121508/sst4/mp3files/29122007/3629de42041775fc1c28ba2169319da3.mp3");
            player.setOnErrorListener(this);
            player.setOnPreparedListener(this);
            player.prepareAsync();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
