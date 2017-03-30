package com.example.odd.ostrino;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * Created by Odd on 29.03.2017.
 */

public class FloatingWindow extends Service {

    private WindowManager wm;
    public RelativeLayout rl;
    private Button btnStop;
    private ImageView iw;
    private String VIDEO_ID;
    private YoutubeFragment youtubeFragment;
    private WindowManager.LayoutParams params;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        rl = new RelativeLayout(this);
        btnStop = new Button(this);
        iw = new ImageView(this);
        VIDEO_ID = "kjGPE_XLmwg";

        ViewGroup.LayoutParams btnParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnStop.setText("Stop");
        btnStop.setLayoutParams(btnParams);

        ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(400, 280);
        iw.setImageResource(R.drawable.tranquility);
        iw.setLayoutParams(imageParams);
        iw.setY(120);


        final LinearLayout.LayoutParams llParameters = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        rl.setBackgroundColor(Color.argb(66, 255, 0, 0));
        rl.setLayoutParams(llParameters);

        youtubeFragment = new YoutubeFragment();
        youtubeFragment.setVideoId("https://www.youtube.com/watch?v=kjGPE_XLmwg");

        params = new WindowManager.LayoutParams(400, 400, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        params.x = 0;
        params.y = 0;
        params.gravity = Gravity.CENTER;

        rl.addView(btnStop);
        rl.addView(iw);
        wm.addView(rl, params);

        rl.setOnTouchListener(new View.OnTouchListener() {

            private WindowManager.LayoutParams updateParams = params;
            int X, Y;
            float touchedX, touchedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        X = updateParams.x;
                        Y = updateParams.y;

                        touchedX = event.getRawX();
                        touchedY = event.getRawY();

                        System.out.println(X + ", " + Y);

                        break;
                    case MotionEvent.ACTION_MOVE:

                        updateParams.x = (int) (X + (event.getRawX() - touchedX));
                        updateParams.y = (int) (Y + (event.getRawY() - touchedY));

                        wm.updateViewLayout(rl, updateParams);

                        break;

                    default:
                        break;
                }

                return false;
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wm.removeView(rl);
                stopSelf();
            }
        });
    }
    public void addView(View view){
        wm.addView(view, params);
    }
}
