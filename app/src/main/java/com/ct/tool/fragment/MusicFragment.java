package com.ct.tool.fragment;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ct.tool.R;


/**
 * Created by Cting on 2016/8/24.
 */
public class MusicFragment extends Fragment implements View.OnClickListener {

    //    alps\packages\apps\Music\src\com\android\music\MediaPlaybackService.java
    //    alps\packages\apps\Music\src\com\android\music\MusicUtils.java
    //    alps\packages\apps\Music\src\com\android\music\MediaButtonIntentReceiver.java

    private static final String TAG = "cting/Music";

    private static final int MSG_UPDATE_PLAY_PAUSE_BUTTON = 0;
    public static final String ACTION_AUDIO_BECOMING_NOISY = "android.media.AUDIO_BECOMING_NOISY";

    AudioManager mAudioMgr;
    ImageView mBtnPlayPause;
    ImageView mBtnPrev;
    ImageView mBtnNext;

    BroadcastReceiver mMediaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "receiver:" + action);
            if (Intent.ACTION_MEDIA_BUTTON.equals(action)) {
                KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                Log.d(TAG, "receiver " + keyEvent.toString());
                int keyAction = keyEvent.getAction();
                if(keyAction==KeyEvent.ACTION_UP){
                    refreshButtonsLater(500);
                }
            } else if (ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
                //earphone unplugged
                refreshButtonsLater(500);
            }
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_PLAY_PAUSE_BUTTON:
                    updateButtonsUi();
                    break;
            }
        }
    };

    public MusicFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioMgr = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        mBtnPlayPause = (ImageView) view.findViewById(R.id.img_play_pause);
        mBtnPlayPause.setOnClickListener(this);
        mBtnPrev = (ImageView) view.findViewById(R.id.img_prev);
        mBtnPrev.setOnClickListener(this);
        mBtnNext = (ImageView) view.findViewById(R.id.img_next);
        mBtnNext.setOnClickListener(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        filter.addAction(ACTION_AUDIO_BECOMING_NOISY);
        getActivity().registerReceiver(mMediaReceiver, filter);
        refreshButtons();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mMediaReceiver);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_play_pause:
                clickPlayPause();
                break;
            case R.id.img_prev:
                clickPrev();
                break;
            case R.id.img_next:
                clickNext();
                break;
        }
    }

    public void refreshButtons() {
        mHandler.sendEmptyMessage(MSG_UPDATE_PLAY_PAUSE_BUTTON);
    }

    public void refreshButtonsLater(long millionSeconds) {
        mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PLAY_PAUSE_BUTTON, millionSeconds);
    }

    private void updateButtonsUi() {
        if (mAudioMgr == null || mBtnPlayPause == null) {
            Log.w(TAG, "updateButtonsUi,mAudioMgr=" + mAudioMgr + ",mBtnPlayPause=" + mBtnPlayPause);
            return;
        }
        boolean isMusicActive = mAudioMgr.isMusicActive();
        Log.d(TAG, "updateButtonsUi,isMusicActive=" + isMusicActive);
        if (isMusicActive) {
            mBtnPlayPause.setImageResource(R.drawable.ic_music_play_black_24px);
        } else {
            mBtnPlayPause.setImageResource(R.drawable.ic_music_pause_black_24px);
        }
    }

    public void clickPlayPause() {
        clickMediaButton(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
    }

    public void clickPrev() {
        clickMediaButton(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
    }

    public void clickNext() {
        clickMediaButton(KeyEvent.KEYCODE_MEDIA_NEXT);
    }

    public void clickMediaButton(int keyCode) {
        Log.i(TAG, "click button," + keyCode);
        reportMediaKey(KeyEvent.ACTION_DOWN, keyCode);
        reportMediaKey(KeyEvent.ACTION_UP, keyCode);
    }

    public void reportMediaKey(int action, int keyCode) {
        KeyEvent keyEvent = new KeyEvent(action, keyCode);
        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
        Log.d(TAG, "reportMediaKey " + keyEvent.toString());
        getActivity().sendBroadcast(intent);
    }

}
