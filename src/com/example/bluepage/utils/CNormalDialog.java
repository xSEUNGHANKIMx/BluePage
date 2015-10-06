package com.example.bluepage.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.bluepage.R;

public class CNormalDialog extends Dialog {

    private TextView mTvTitle = null;
    private TextView mTvBody = null;
    private Button mPositiveBtn = null;
    private Button mNegativeBtn = null;

    public static final int TYPE_CONTACTS_ITEM_DELETE = 0;           //연락처 항목 삭제
    public static final int TYPE_CALL_CONNECTION_REQUEST = 1; // 음성PTT, 영상PTT, 음성통화, 영상통화 요청
    public static final int TYPE_CALL_CONNECTION_FAIL = 2;           //통화 연결 불가
    public static final int TYPE_CALL_ADD_CONNECTION = 3;           //통화 연결
    public static final int TYPE_CALL_CHANGE = 4;           //통화 전환
    public static final int TYPE_CALL_EXIT = 5;           //나가기
    public static final int TYPE_CALL_CHANGE_RECORDING_CANCEL = 6;   //녹음 취소 ( 채널  변경 )
    public static final int TYPE_CALL_RECORDING_CANCEL = 7;           //녹음 취소 ( 종료 버튼 클릭 )
    public static final int TYPE_CALL_DIALER_RECORDING_CANCEL = 8;   //녹음 취소 ( 통화 걸때 )
    public static final int TYPE_CALL_FAIL = 10;                      //통화 연결 불가
    public static final int TYPE_SETTING_LOGOUT = 11;                   //로그아웃
    public static final int TYPE_SETTING_GPS = 12;                      //GPS
    public static final int TYPE_CALL_PROGRESS = 13;                    //통화 연결중
    public static final int TYPE_CALL_MAX = 14;
    public static final int TYPE_CALL_LOG_DELETE = 15;  // Call Log 삭제
    public static final int TYPE_CALL_LOG_RETRY = 16;  // Retry Failed Outgoing Call

    private OnPositiveListener mPositiveListener = null;
    private OnNegativeListener mNegativeListener = null;

    private int mType = -1;
    private String mTitle = null;
    private String mBody = null;

    private Context mContext = null;

    public CNormalDialog(Context context) {
        super(context);
        mContext = context;
    }

    public CNormalDialog(Context context, int type) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        mType = type;
    }

    public CNormalDialog(Context context, int type, String body) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        mType = type;
        mBody = body;
    }

    public CNormalDialog(Context context, int type, String body, OnPositiveListener p, OnNegativeListener n) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        mType = type;
        mBody = body;
        mPositiveListener = p;
        mNegativeListener = n;
    }

    private void init(Context context, int type, String body, OnPositiveListener p, OnNegativeListener n) {
        if (context == null) {
            return;
        }

        mPositiveListener = p;
        mNegativeListener = n;

        switch (type) {
            case TYPE_CONTACTS_ITEM_DELETE:     //연락처 항목 삭제
                setTitle(R.string.dialog_delete_title);
                setMessage(R.string.dialog_item_delete_body);
                mPositiveBtn.setText(R.string.dialog_btn_delete);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
            case TYPE_CALL_CONNECTION_REQUEST:  //통화 요청
                setTitle(R.string.call_connection_notice);
                if (TextUtils.isEmpty(body)) {
                    setMessage(context.getString(R.string.call_connection_request, "unknown", "unknown"));
                } else {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.dialog_btn_connection);
                mNegativeBtn.setText(R.string.dialog_btn_refusal);
                break;
            case TYPE_CALL_CONNECTION_FAIL:     //통화 연결 불가
                //1. 10개 채널이 모두 pre-arranged 통화이며 add-hoc으로 통화연결시 호출
                //2. pre-arranged로 통화연결시 연결요청한 채널이 기존 채널보다 우선순위가 낮을 경우 호출
                setTitle(R.string.call_connection_fail_notice);
                if (TextUtils.isEmpty(body)) {
                    setMessage(context.getString(R.string.call_limit_connection_fail, "unknown"));
                } else {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.dialog_btn_positive);
                break;
            case TYPE_CALL_ADD_CONNECTION:
                //1. 10개 채널이 모두 통화중 일때 연결된 채널중 add-hoc채널이 있다면 호출
                //2. pre_arranged로 통화연결시 연결된 채널보다 우선순위가 높다면 호출
                setTitle(R.string.call_connection_notice);
                if (TextUtils.isEmpty(body)) {
                    setMessage(context.getString(R.string.call_limit_connection, "unknown", "unknown"));
                } else {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.dialog_btn_connection);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
            case TYPE_CALL_CHANGE:
                //음성 PTT, 영상 PTT, 음성 통화, 영상 통화 전환시 호출
                setTitle(R.string.call_change);
                if (TextUtils.isEmpty(body)) {
                    setMessage(context.getString(R.string.call_change_body2));
                } else {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.dialog_btn_change);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
            case TYPE_CALL_EXIT:
                //참여중인 채널에서 나가기를 선택하면 호출
                setTitle(R.string.call_exit);
                if (TextUtils.isEmpty(body)) {
                    setMessage(context.getString(R.string.call_exit_body, "unknown"));
                } else {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.dialog_btn_positive);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
            case TYPE_CALL_CHANGE_RECORDING_CANCEL:
                //녹음중 통화전환시에 호출
                setTitle(R.string.call_recording_cancel);
                setMessage(context.getString(R.string.call_recording_cancel_body));
                mPositiveBtn.setText(R.string.dialog_btn_change);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
            case TYPE_CALL_RECORDING_CANCEL:
                //녹음중 통화종료시에 호출
                setTitle(R.string.call_recording_cancel);
                setMessage(context.getString(R.string.call_end_recording_cancel_body));
                mPositiveBtn.setText(R.string.dialog_btn_positive);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
            case TYPE_CALL_DIALER_RECORDING_CANCEL:
                //녹음중 다이얼러를 통하여 전화를 걸때
                setTitle(R.string.call_recording_cancel);
                setMessage(context.getString(R.string.call_dialer_recording_cancel_body));
                mPositiveBtn.setText(R.string.dialog_btn_positive);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
            case TYPE_CALL_FAIL:
                setTitle(R.string.call_connection_fail_notice);
                if (TextUtils.isEmpty(body)) {
                    setMessage(context.getString(R.string.call_limit_connection_fail, "unknown"));
                } else {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.dialog_btn_positive);
                break;

            case TYPE_SETTING_LOGOUT:
                setTitle(R.string.logout);
                setMessage(context.getString(R.string.logout_question));
                mNegativeBtn.setText(R.string.btn_cancel);
                mPositiveBtn.setText(R.string.btn_log_out_ok);
                break;

            case TYPE_SETTING_GPS:
                setTitle(R.string.position_setting);
                setMessage(context.getString(R.string.position_setting_question));
                mNegativeBtn.setText(R.string.btn_gps_cancel);
                mPositiveBtn.setText(R.string.btn_gps_ok);
                break;
            case TYPE_CALL_PROGRESS:
                setTitle(R.string.call_progress_title);
                if (!TextUtils.isEmpty(body)) {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.btn_cancel);
                mNegativeBtn.setVisibility(View.GONE);
                break;
            case TYPE_CALL_MAX:
                setTitle(R.string.call_max_title);
                if (!TextUtils.isEmpty(body)) {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.dialog_btn_change);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
            case TYPE_CALL_LOG_DELETE:
                setTitle(R.string.dialog_delete_title);
                if (!TextUtils.isEmpty(body)) {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.dialog_delete_title);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
            case TYPE_CALL_LOG_RETRY:
                setTitle(R.string.calllog_retry_failed_outgoing_call_title);
                if (!TextUtils.isEmpty(body)) {
                    setMessage(body);
                }
                mPositiveBtn.setText(R.string.calllog_retry_failed_outgoing_call_button);
                mNegativeBtn.setText(R.string.dialog_btn_cancel);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND | WindowManager.LayoutParams.FLAG_FULLSCREEN;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.normaldialog_layout);
        mTvTitle = (TextView) findViewById(R.id.dialog_title);
        mTvBody = (TextView) findViewById(R.id.dialog_body);
        mNegativeBtn = (Button) findViewById(R.id.btn_negative);
        mPositiveBtn = (Button) findViewById(R.id.btn_positive);
        mPositiveBtn.setOnClickListener(getPositiveButtonListener);
        mNegativeBtn.setOnClickListener(getNegativeButtonListener);

        init(mContext, mType, mBody, mPositiveListener, mNegativeListener);

    }

    private android.view.View.OnClickListener getPositiveButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mPositiveListener != null) {
                mPositiveListener.onPositive();
            }
            dismiss();
        }
    };

    private android.view.View.OnClickListener getNegativeButtonListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mNegativeListener != null) {
                mNegativeListener.onNegative();
            }
            dismiss();
        }
    };

    @Override
    public void setTitle(int titleId) {
        mTitle = mContext.getResources().getString(titleId);
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setMessage(int messageId) {
        mBody = mContext.getString(messageId);
    }

    public void setMessage(String message) {
        mBody = message;
    }

    @Override
    public void show() {
        super.show();
        mTvTitle.setText(mTitle);
        mTvBody.setText(mBody);
    }

    public interface OnPositiveListener {
        public abstract void onPositive();
    }

    public void setOnPositiveListener(OnPositiveListener listener) {
        mPositiveListener = listener;
    }

    public interface OnNegativeListener {
        public abstract void onNegative();
    }

    public void setOnNegativeListener(OnNegativeListener listener) {
        mNegativeListener = listener;
    }

    public static final String getExitBody(Context context, String name) {
        if (context == null) {
            return null;
        }
        String result = null;
        if (!TextUtils.isEmpty(name)) {
            result = context.getString(R.string.call_exit_body, name);
        } else {
            result = context.getString(R.string.call_exit_body, "unknown");
        }

        return result;
    }
}
