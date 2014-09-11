package me.doapps.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import me.doapps.beans.Music_DTO;
import me.doapps.descargarmp3.R;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Music_Adapter extends BaseAdapter {
    private ArrayList<Music_DTO> music_dtos;
    private Context context;
    private LayoutInflater inflater;

    public Music_Adapter(ArrayList<Music_DTO> music_dtos, Context context){
        this.music_dtos = music_dtos;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return music_dtos.size();
    }

    @Override
    public Object getItem(int i) {
        return music_dtos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        Music_DTO music_dto = music_dtos.get(i);

        if(view == null){
            view = inflater.inflate(R.layout.item_music, viewGroup, false);
            holder = new Holder();

            holder.txt_name_music = (TextView)view.findViewById(R.id.txt_name_music);
            holder.txt_duration_music = (TextView)view.findViewById(R.id.txt_duration_music);
            view.setTag(holder);
        }
        else {
            holder = (Holder)view.getTag();
        }
        holder.txt_name_music.setText(music_dto.getName());
        holder.txt_duration_music.setText(music_dto.getDuration());

        return view;
    }

    class Holder{
        TextView txt_name_music;
        TextView txt_duration_music;
    }
}
