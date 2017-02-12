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

public class OstObject {
    private String title, show;
    private List tags = new ArrayList<>();

    public OstObject(String title, String show, List tags) {
        this.title = title;
        this.show = show;
        for (Object tag : tags) {
            //System.out.println(tag);
            this.tags.add(tag);
        }
    }

    @Override
    public String toString() {
        return "OstObject{" +
                "title='" + title + '\'' +
                ", show='" + show + '\'' +
                ", tags=" + tags +
                '}';
    }
}
