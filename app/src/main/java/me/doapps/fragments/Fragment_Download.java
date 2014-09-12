package me.doapps.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.viewpagerindicator.PageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import me.doapps.adapters.Fragment_Adapter;
import me.doapps.descargarmp3.R;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Fragment_Download extends Fragment {
    private Button btn_downloader;
    private MediaPlayer mediaPlayer;

    private ProgressDialog progressDialog;

    public static final int progress_bar_type = 0;
    private static String file_url = "http://programmerguru.com/android-tutorial/wp-content/uploads/2014/01/jai_ho.mp3";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_download, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_downloader = (Button)getView().findViewById(R.id.btn_downloader);
        btn_downloader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_downloader.setEnabled(false);
                File file = new File(Environment.getExternalStorageDirectory().getPath()+"/jjjooina.mp3");
                if(file.exists()){
                    Toast.makeText(getActivity(), "ya existe", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getActivity(), "no existe, descargando...", Toast.LENGTH_SHORT).show();
                    new DownloadMusicfromInternet().execute(file_url);
                }
            }
        });
    }



    // Async Task Class
    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            //showDialog(progress_bar_type);
            progressDialog = ProgressDialog.show(getActivity(), null, "Wait please...", true);
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
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/jjjooina.mp3");
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
            //dismissDialog(progress_bar_type);
            progressDialog.hide();
            Toast.makeText(getActivity(), "Download complete, playing Music", Toast.LENGTH_LONG).show();
            // Play the music
            playMusic();
        }
    }

    // Play Music
    protected void playMusic(){
        // Read Mp3 file present under SD card
        Uri myUri1 = Uri.parse("file:///sdcard/jjjooina.mp3");
        mediaPlayer  = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getActivity(), myUri1);
            mediaPlayer.prepare();
            // Start playing the Music file
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    // Once Music is completed playing, enable the button
                    btn_downloader.setEnabled(true);
                    Toast.makeText(getActivity(), "Music completed playing",Toast.LENGTH_LONG).show();
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(getActivity(), "You might not set the URI correctly!",    Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getActivity(),    "URI cannot be accessed, permissed needed",    Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getActivity(),    "Media Player is not in correct state",    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getActivity(),    "IO Error occured",    Toast.LENGTH_LONG).show();
        }
    }
}
