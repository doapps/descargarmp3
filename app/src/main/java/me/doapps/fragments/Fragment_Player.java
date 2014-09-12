package me.doapps.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

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

    private Button btn_download;
    private ProgressDialog progressDialog;

    private String url_temp = "";
    private String music_name = "";

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

        /*create folder*/
        File wallpaperDirectory = new File("/sdcard/descargamp3/");
        wallpaperDirectory.mkdirs();

        music_dto = (Music_DTO) getActivity().getIntent().getSerializableExtra("music_dto");
        url_temp = music_dto.getUrl();
        music_name = music_dto.getName()+".mp3";
        try {
            music_name = URLEncoder.encode(music_name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ((TextView) getView().findViewById(R.id.nombremusica)).setText(music_dto.getName());

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

        btn_download = (Button)getView().findViewById(R.id.btn_download);
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_download.setEnabled(false);
                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/descargamp3/"+music_name);
                if(file.exists()){
                    Toast.makeText(getActivity(), "Archivo ya existe", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getActivity(), "no existe, descargando...", Toast.LENGTH_SHORT).show();
                    new DownloadMusicfromInternet().execute(url_temp);
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

    /*Async Class - Download*/
    // Async Task Class
    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            progressDialog = ProgressDialog.show(getActivity(), null, "Descargando...", true);
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(),10*1024);
                // Output stream to write file in SD card
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/descargamp3/"+music_name);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // While Downloading Music File
        protected void onProgressUpdate(String... progress) {
            // Set progress percentage
            //dialog.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            // Dismiss the dialog after the Music file was downloaded
            progressDialog.hide();
            Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
        }
    }

}
