package me.doapps.beans;

import org.json.JSONObject;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Music_DTO {


    private String name;
    private String url;
    private String duration;
    private JSONObject jsonObjectTrack;

    public Music_DTO(){}

    public Music_DTO(String name, String duration, String url, JSONObject jsonObjectTrack) {
        this.name = name;
        this.duration = duration;
        this.url = url;
        this.jsonObjectTrack = jsonObjectTrack;
    }

    public Music_DTO(String name, String duration, String url) {
        this.name = name;
        this.duration = duration;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public JSONObject getJsonObjectTrack() {
        return jsonObjectTrack;
    }

    public void setJsonObjectTrack(JSONObject jsonObjectTrack) {
        this.jsonObjectTrack = jsonObjectTrack;
    }
}
