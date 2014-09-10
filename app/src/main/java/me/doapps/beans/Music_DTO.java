package me.doapps.beans;

/**
 * Created by jnolascob on 10/09/2014.
 */
public class Music_DTO {
    private String name;
    private String url;

    public Music_DTO(){}

    public Music_DTO(String name, String url){
        this.name = name;
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
}
