package com.example.odd.ostrino;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by Odd on 12.02.2017.
 */

public class Ost {
    private String title, show, tags, url;

    private int id;

    public Ost(){};

    public Ost(String title, String show, String tags, String url) {
        this.title = title;
        this.show = show;
        this.tags = tags;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Ost{" +
                "id=" + id + '\'' +
                "title='" + title + '\'' +
                ", show='" + show + '\'' +
                ", tags=" + tags + '\'' +
                ", url=" + url +
                '}';
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url){
        this.url = url;
    }
}
