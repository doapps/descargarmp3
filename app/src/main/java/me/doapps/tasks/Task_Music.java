package me.doapps.tasks;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.doapps.beans.Music_DTO;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Task_Music {
    private Context context;
    private Interface_Music interface_music;
    private static final String WS_ALL_MUSIC = "http://api.mp3yox.com/tracks/audio.php";

    public Task_Music(Context context){
        this.context = context;
    }

    public void sendRequestMusics(final String music_name){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.GET,
                WS_ALL_MUSIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            ArrayList<Music_DTO> music_dtos = new ArrayList<Music_DTO>();

                            JSONArray jsonArrayMusic = new JSONArray(response);
                            Log.e("list music", jsonArrayMusic.toString());
                            if(jsonArrayMusic.length()>0){
                                for (int i = 0; i < jsonArrayMusic.length(); i++) {
                                    String temp_title = jsonArrayMusic.getJSONObject(i).getString("title").toString();
                                    String temp_url = jsonArrayMusic.getJSONObject(i).getString("mp3_url").toString();
                                    Log.e("title", jsonArrayMusic.getJSONObject(i).getString("title"));
                                    Log.e("mp3_url", jsonArrayMusic.getJSONObject(i).getString("mp3_url"));
                                    music_dtos.add(new Music_DTO(temp_title, temp_url));
                                }
                                interface_music.getMusic(true, music_dtos);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(context, "Ocurrio un error de Conexion", Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("q",music_name);
                return params;
            }
        };
        queue.add(jsonStringRequest);
    }


    /*Music list*/
    public void setInterface_music(Interface_Music interface_music){
        this.interface_music = interface_music;
    }

    public interface Interface_Music{
        void getMusic(boolean status, ArrayList<Music_DTO> music_dtos);
    }
}


