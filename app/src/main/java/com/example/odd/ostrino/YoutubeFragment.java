package com.example.odd.ostrino;

/**
 * Created by Odd on 22.03.2017.
 */


import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioTrack;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;


public class YoutubeFragment extends Fragment implements OnInitializedListener, YouTubePlayer.OnFullscreenListener{
    // API キー
    private static final String API_KEY = "AIzaSyDSMKvbGUJxKhPz5t4PMFEByD5qFy1sjEA";

    @SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

    // YouTubeのビデオID
    private String videoId;
    private List<String> videoIds;
    private boolean playQueue = false;
    private YouTubePlayer mPlayer = null;
    private boolean mAutoRotation = false;

    public YoutubeFragment(){}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.youtube_api, container, false);
        mAutoRotation = Settings.System.getInt(getActivity().getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1;

        // YouTubeフラグメントインスタンスを取得
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();

        // レイアウトにYouTubeフラグメントを追加
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_layout, youTubePlayerFragment).commit();

        // YouTubeフラグメントのプレーヤーを初期化する
        youTubePlayerFragment.initialize(API_KEY, this);

        return rootView;
    }

    // YouTubeプレーヤーの初期化成功
    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean wasRestored) {
        mPlayer = player;
        //player.setPlayerStateChangeListener(this);
        player.setOnFullscreenListener(this);
        if (mAutoRotation) {
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        } else {
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                    | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                    | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        }
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            player.setManageAudioFocus(true);
            if(playQueue){
                mPlayer.loadVideos(videoIds);

            }else{
                mPlayer.loadVideo(videoId);
            }
            mPlayer.play();
    }

    // YouTubeプレーヤーの初期化失敗
    @Override
    public void onInitializationFailure(Provider provider, YouTubeInitializationResult error) {
        // YouTube error
        String errorMessage = error.toString();
        Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_LONG).show();
        Log.d("errorMessage:", errorMessage);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (mPlayer != null)
                mPlayer.setFullscreen(true);
        }
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (mPlayer != null)
                mPlayer.setFullscreen(false);
        }
    }

    @Override
    public void onFullscreen(boolean fullsize) {
        if (fullsize) {
            getActivity().setRequestedOrientation(LANDSCAPE_ORIENTATION);
        } else {
            getActivity().setRequestedOrientation(PORTRAIT_ORIENTATION);
        }
    }
    public void setVideoId(String url){
        videoId = urlToId(url);
    }

    public void setVideoIds(List<String> urls){
        videoIds = new ArrayList<>();
        for(String url : urls){
            String id = urlToId(url);
            videoIds.add(id);
        }
        System.out.println("videoIDs: " + videoIds.toString());
    }

    public void playAll(boolean playlist){
        this.playQueue = playlist;
    }

    public String urlToId(String url){
        String [] lineArray;
        if(url.contains("&")){
            lineArray = url.split("&");
            lineArray = lineArray[0].split("=");
        } else if(url.contains("be/")){
            lineArray = url.split("be/");
        }else{
            lineArray = url.split("=");
        }
        return videoId = lineArray[1];
    }
}