package com.example.odd.ostrino;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

/**
 * Created by Odd on 22.03.2017.
 */

public class YoutubePlayerActivity extends YouTubeBaseActivity  implements YouTubePlayer.OnInitializedListener{



    private YouTubePlayerView playerView;
    private YouTubePlayer player;
    private int RECOVERY_DIALOG_REQUEST = 1;
    private String EXTRA_VIDEO_ID = "video_id";
    private String videoId;

    private String YOUTUBE_DEVELOPER_KEY = "AIzaSyDSMKvbGUJxKhPz5t4PMFEByD5qFy1sjEA";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        playerView = new YouTubePlayerView(this);
        playerView.initialize(YOUTUBE_DEVELOPER_KEY, this);
        videoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);

        if (videoId == null)
            throw new NullPointerException("Video ID must not be null");
        addContentView(playerView, new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT));

        playerView.setBackgroundResource(android.R.color.black);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        player = youTubePlayer;
        if (!wasRestored)
            player.loadVideo("fhWaJi1Hsfo");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    "There was an error initializing the YouTubePlayer (%1$s)",
                    errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }
}
