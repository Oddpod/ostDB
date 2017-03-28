package com.example.odd.ostrino;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YouTubePlayerActivityFragment extends YouTubePlayerSupportFragment implements
    YouTubePlayer.OnFullscreenListener {

    @SuppressLint("InlinedApi")
    private static final int PORTRAIT_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;

    @SuppressLint("InlinedApi")
    private static final int LANDSCAPE_ORIENTATION = Build.VERSION.SDK_INT < 9
            ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            : ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;

    private YouTubePlayer.OnFullscreenListener fullScreenListener = null;
    private YouTubePlayer yPlayer = null;
    private boolean mAutoRotation = false;


    public static YouTubePlayerActivityFragment newInstance(String videoID, String apiKey) {
        YouTubePlayerActivityFragment fragment = new YouTubePlayerActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("video_id", videoID);
        bundle.putString("api_key", apiKey);
        fragment.setArguments(bundle);
        fragment.init();
        return fragment;
    }

    public YouTubePlayerActivityFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init() {
        initialize(getArguments().getString("api_key"), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                yPlayer = youTubePlayer;
                youTubePlayer.setOnFullscreenListener(fullScreenListener);
                if (mAutoRotation) {
                    youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                            | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                            | YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE
                            | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                } else {
                    youTubePlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION
                            | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI
                            | YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                }

                if (!wasRestored) {
                    yPlayer.loadVideo(getArguments().getString("video_id"));
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public void onFullscreen(boolean isFullSize) {
        if (isFullSize) {
            getActivity().setRequestedOrientation(LANDSCAPE_ORIENTATION);
        } else {
            getActivity().setRequestedOrientation(PORTRAIT_ORIENTATION);
        }
    }
}