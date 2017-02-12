package com.example.odd.ostrino;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Odd on 12.02.2017.
 */

public class listScreen {

   public listScreen() {
       List tags = new ArrayList<String>();
       tags.add("KANA_BOON");
       tags.add("Upbeat");
       OstObject ost = new OstObject("Silhouette", "Naruto", tags);
       System.out.println(ost);
   }
}
