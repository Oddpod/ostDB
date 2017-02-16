package com.example.odd.ostrino;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Created by Odd on 12.02.2017.
 */

public class Ost {
    private String title, show, tags;

    private int id;

    public Ost(){};

    public Ost(String title, String show, String tags) {
        this.title = title;
        this.show = show;
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Ost{" +
                "title='" + title + '\'' +
                ", show='" + show + '\'' +
                ", tags=" + tags +
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
}
