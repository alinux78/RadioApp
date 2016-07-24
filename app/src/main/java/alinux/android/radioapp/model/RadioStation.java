package alinux.android.radioapp.model;

import java.io.Serializable;

/**
 * Created by mihai on 7/23/16.
 */
public class RadioStation implements Serializable{

    private String name;
    private String url;


    public RadioStation(String name, String url) {
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
