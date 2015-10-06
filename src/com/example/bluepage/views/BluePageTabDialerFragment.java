package com.example.bluepage.views;

import com.example.bluepage.R;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** 전화걸기 Activity
 *
 * @author jihun.cho */
public class BluePageTabDialerFragment extends Fragment implements OnClickListener, OnLongClickListener {

    private static final String LOG_TAG = BluePageTabDialerFragment.class.getSimpleName();

    private static BluePageTabDialerFragment sInstance;
    private TextView tvKeyPressView = null;
    private String strKeyMsg = null;
    private static final int PTT_NUM_LENGTH = 7;

    public static synchronized BluePageTabDialerFragment getInstance() {
        if (sInstance == null) {
            sInstance = new BluePageTabDialerFragment();
        }
        return sInstance;
    }

    public static synchronized void removeInstance() {
        sInstance = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.call_dialler_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tvKeyPressView = (TextView) getView().findViewById(R.id.phone_key_num_view_txt);
        tvKeyPressView.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        strKeyMsg = "";
        tvKeyPressView.setText(strKeyMsg);

        ((LinearLayout) getView().findViewById(R.id.phone_key_1)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_2)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_3)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_4)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_5)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_6)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_7)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_8)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_9)).setOnClickListener(this);

        ((LinearLayout) getView().findViewById(R.id.phone_key_star)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_0)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_sharp)).setOnClickListener(this);

        ((RelativeLayout) getView().findViewById(R.id.phone_key_voice_call)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_message)).setOnClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_del)).setOnClickListener(this);

        ((LinearLayout) getView().findViewById(R.id.phone_key_star)).setOnLongClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_sharp)).setOnLongClickListener(this);
        ((LinearLayout) getView().findViewById(R.id.phone_key_del)).setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int ids = v.getId();
        switch (ids) {
            case R.id.phone_key_0:
                strKeyMsg += "0";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_1:
                strKeyMsg += "1";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_2:
                strKeyMsg += "2";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_3:
                strKeyMsg += "3";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_4:
                strKeyMsg += "4";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_5:
                strKeyMsg += "5";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_6:
                strKeyMsg += "6";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_7:
                strKeyMsg += "7";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_8:
                strKeyMsg += "8";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_9:
                strKeyMsg += "9";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_del:
                if ((strKeyMsg != null) && (strKeyMsg.length() > 0)) {
                    strKeyMsg = strKeyMsg.substring(0, strKeyMsg.length() - 1);
                    tvKeyPressView.setText(strKeyMsg);
                }
                break;
            case R.id.phone_key_star:
                strKeyMsg += "*";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_sharp:
                strKeyMsg += "#";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_voice_call:
                break;
            case R.id.phone_key_message:
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onLongClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.phone_key_del:
                strKeyMsg = "";
                tvKeyPressView.setText(strKeyMsg);
                return true;
            case R.id.phone_key_star:
                strKeyMsg += ";";
                tvKeyPressView.setText(strKeyMsg);
                break;
            case R.id.phone_key_sharp:
                AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                } else {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                return true;
            default:
                break;
        }
        return false;
    }


}
