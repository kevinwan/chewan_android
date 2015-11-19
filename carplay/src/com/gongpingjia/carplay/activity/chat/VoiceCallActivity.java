/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gongpingjia.carplay.activity.chat;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.chat.EMCallStateChangeListener;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EMServiceNotReadyException;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.gongpingjia.carplay.util.Utils;
import com.gongpingjia.carplay.view.AnimButtonView2;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * 语音通话页面
 */
public class VoiceCallActivity extends CallActivity implements OnClickListener {
    private LinearLayout comingBtnContainer;
    private ImageView hangupBtn;
    private ImageView refuseBtn;
    private ImageView answerBtn;
    private ImageView muteImage;
    private ImageView handsFreeImage;

    private boolean isMuteState;
    private boolean isHandsfreeState;

    private TextView callStateTextView;
    private int streamID;
    private boolean endCallTriggerByMe = false;
    private Handler handler = new Handler();
    private TextView nickTextView;
    private TextView durationTextView;
    private TextView txt_mute;
    private TextView txt_handsfree;
    private Chronometer chronometer;
    String st1;
    private boolean isAnswered;
    private LinearLayout answering_layout, call_layout;
    //	private LinearLayout voiceContronlLayout;
    AnimButtonView2 swing_card;
    private Vibrator vibrator;
    long[] pattern = {1000, 2000, 1000, 2000, 1000, 2000, 1000, 2000};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        setContentView(R.layout.activity_voice_call);

        HXSDKHelper.getInstance().isVoiceCalling = true;
        swing_card = (AnimButtonView2) findViewById(R.id.swing_card);
//		comingBtnContainer = (LinearLayout) findViewById(R.id.ll_coming_call);
        refuseBtn = (ImageView) findViewById(R.id.btn_refuse_call);
        answerBtn = (ImageView) findViewById(R.id.btn_answer_call);
        hangupBtn = (ImageView) findViewById(R.id.btn_hangup_call);
        muteImage = (ImageView) findViewById(R.id.iv_mute);
        handsFreeImage = (ImageView) findViewById(R.id.iv_handsfree);
        callStateTextView = (TextView) findViewById(R.id.tv_call_state);
        nickTextView = (TextView) findViewById(R.id.tv_nick);
        txt_handsfree = (TextView) findViewById(R.id.txt_handsfree);
        txt_mute = (TextView) findViewById(R.id.txt_mute);
        durationTextView = (TextView) findViewById(R.id.tv_calling_duration);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        //接听、挂断
        answering_layout = (LinearLayout) findViewById(R.id.answering_layout);
        //接听后、免提、静音
        call_layout = (LinearLayout) findViewById(R.id.call_layout);

        refuseBtn.setOnClickListener(this);
        answerBtn.setOnClickListener(this);
        hangupBtn.setOnClickListener(this);
        muteImage.setOnClickListener(this);
        handsFreeImage.setOnClickListener(this);

        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        // 注册语音电话的状态的监听
        addCallStateListener();
        msgid = UUID.randomUUID().toString();

        username = getIntent().getStringExtra("username");
        // 语音电话是否为接收的
        isInComingCall = getIntent().getBooleanExtra("isComingCall", false);

        // 设置通话人
        nickTextView.setText(username);
        DhNet dhNet = new DhNet(API2.getProfileFromHx(User.getInstance().getUserId(), User.getInstance().getToken(), username));
        dhNet.doGet(new NetTask(self) {
            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    JSONObject jsonObject = response.jSONFrom("data");
                    try {
                        nickTextView.setText(jsonObject.getString("nickname"));
                        ImageLoader.getInstance().displayImage(jsonObject.getString("avatar"), swing_card.getRealImage());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (!isInComingCall) {// 拨打电话
            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
            outgoing = soundPool.load(this, R.raw.callringdudu, 1);

//			comingBtnContainer.setVisibility(View.INVISIBLE);
            answering_layout.setVisibility(View.GONE);
            call_layout.setVisibility(View.VISIBLE);
            hangupBtn.setVisibility(View.VISIBLE);
            st1 = getResources()
                    .getString(R.string.Are_connected_to_each_other);
            callStateTextView.setText(st1);
            handler.postDelayed(new Runnable() {
                public void run() {
                    streamID = playMakeCallSounds();
                }
            }, 300);
            try {
                // 拨打语音电话
                EMChatManager.getInstance().makeVoiceCall(username);
            } catch (EMServiceNotReadyException e) {
                e.printStackTrace();
                final String st2 = getResources().getString(
                        R.string.Is_not_yet_connected_to_the_server);
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(VoiceCallActivity.this, st2, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else { // 有电话进来
//			voiceContronlLayout.setVisibility(View.INVISIBLE);
//            answering_layout.setVisibility(View.INVISIBLE);
            answering_layout.setVisibility(View.VISIBLE);
            call_layout.setVisibility(View.GONE);

            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {
                return;
            } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
                outgoing = soundPool.load(this, R.raw.cpcallring, 1);
                handler.postDelayed(new Runnable() {
                    public void run() {
                        streamID = playMakeCallSounds();
                    }
                }, 300);
            } else {
                vibrator = (Vibrator) getSystemService(self.VIBRATOR_SERVICE);
                Utils.Vibrate(self, pattern, true);
            }


//            Uri ringUri = RingtoneManager
//                    .getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//            audioManager.setMode(AudioManager.MODE_RINGTONE);
//            audioManager.setSpeakerphoneOn(true);
//            ringtone = RingtoneManager.getRingtone(this, ringUri);
//            ringtone.play();
        }

    }

    /**
     * 设置电话监听
     */
    void addCallStateListener() {
        callStateListener = new EMCallStateChangeListener() {

            @Override
            public void onCallStateChanged(CallState callState, CallError error) {
                // Message msg = handler.obtainMessage();
                switch (callState) {

                    case CONNECTING: // 正在连接对方
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                callStateTextView.setText(st1);
                            }

                        });
                        break;
                    case CONNECTED: // 双方已经建立连接
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                String st3 = getResources().getString(
                                        R.string.have_connected_with);
                                callStateTextView.setText(st3);
                            }

                        });
                        break;

                    case ACCEPTED: // 电话接通成功
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    if (soundPool != null)
                                        soundPool.stop(streamID);
                                } catch (Exception e) {
                                }
                                if (!isHandsfreeState)
                                    closeSpeakerOn();
                                // 显示是否为直连，方便测试
                                ((TextView) findViewById(R.id.tv_is_p2p))
                                        .setText(EMChatManager.getInstance()
                                                .isDirectCall() ? R.string.direct_call
                                                : R.string.relay_call);
                                chronometer.setVisibility(View.INVISIBLE);
                                chronometer.setBase(SystemClock.elapsedRealtime());
                                // 开始记时
                                chronometer.start();
                                String str4 = getResources().getString(
                                        R.string.In_the_call);
                                callStateTextView.setText(str4);
                                callingState = CallingState.NORMAL;
                            }

                        });
                        break;
                    case DISCONNNECTED: // 电话断了
                        final CallError fError = error;
                        runOnUiThread(new Runnable() {
                            private void postDelayedCloseMsg() {
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        saveCallRecord(0);
                                        Animation animation = new AlphaAnimation(
                                                1.0f, 0.0f);
                                        animation.setDuration(800);
                                        findViewById(R.id.root_layout)
                                                .startAnimation(animation);
                                        finish();
                                    }

                                }, 200);
                            }

                            @Override
                            public void run() {
                                chronometer.stop();
                                callDruationText = chronometer.getText().toString();
                                String st2 = getResources().getString(
                                        R.string.The_other_party_refused_to_accept);
                                String st3 = getResources().getString(
                                        R.string.Connection_failure);
                                String st4 = getResources().getString(
                                        R.string.The_other_party_is_not_online);
                                String st5 = getResources().getString(
                                        R.string.The_other_is_on_the_phone_please);

                                String st6 = getResources()
                                        .getString(
                                                R.string.The_other_party_did_not_answer_new);
                                String st7 = getResources().getString(
                                        R.string.hang_up);
                                String st8 = getResources().getString(
                                        R.string.The_other_is_hang_up);

                                String st9 = getResources().getString(
                                        R.string.did_not_answer);
                                String st10 = getResources().getString(
                                        R.string.Has_been_cancelled);
                                String st11 = getResources().getString(
                                        R.string.hang_up);

                                if (fError == CallError.REJECTED) {
                                    callingState = CallingState.BEREFUESD;
                                    callStateTextView.setText(st2);
                                } else if (fError == CallError.ERROR_TRANSPORT) {
                                    callStateTextView.setText(st3);
                                } else if (fError == CallError.ERROR_INAVAILABLE) {
                                    callingState = CallingState.OFFLINE;
                                    callStateTextView.setText(st4);
                                } else if (fError == CallError.ERROR_BUSY) {
                                    callingState = CallingState.BUSY;
                                    callStateTextView.setText(st5);
                                } else if (fError == CallError.ERROR_NORESPONSE) {
                                    callingState = CallingState.NORESPONSE;
                                    callStateTextView.setText(st6);
                                } else {
                                    if (isAnswered) {
                                        callingState = CallingState.NORMAL;
                                        if (endCallTriggerByMe) {
                                            // callStateTextView.setText(st7);
                                        } else {
                                            callStateTextView.setText(st8);
                                        }
                                    } else {
                                        if (isInComingCall) {
                                            callingState = CallingState.UNANSWERED;
                                            callStateTextView.setText(st9);
                                        } else {
                                            if (callingState != CallingState.NORMAL) {
                                                callingState = CallingState.CANCED;
                                                callStateTextView.setText(st10);
                                            } else {
                                                callStateTextView.setText(st11);
                                            }
                                        }
                                    }
                                }
                                postDelayedCloseMsg();
                            }

                        });

                        break;

                    default:
                        break;
                }

            }
        };
        EMChatManager.getInstance().addCallStateChangeListener(
                callStateListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_refuse_call: // 拒绝接听

                refuseBtn.setEnabled(false);

                try {
                    if (soundPool != null)
                        soundPool.stop(streamID);
                } catch (Exception e) {
                }

                if (ringtone != null) {
                    ringtone.stop();
                }
                if (vibrator != null) {
                    vibrator.cancel();
                }
                try {
                    EMChatManager.getInstance().rejectCall();
                } catch (Exception e1) {
                    e1.printStackTrace();
                    saveCallRecord(0);
                    finish();
                }
                callingState = CallingState.REFUESD;
                break;

            case R.id.btn_answer_call: // 接听电话

                answerBtn.setEnabled(false);

                try {
                    if (soundPool != null)
                        soundPool.stop(streamID);
                } catch (Exception e) {
                }
                if (ringtone != null)
                    ringtone.stop();
                if (vibrator != null) {
                    vibrator.cancel();
                }
                if (isInComingCall) {
                    try {
                        callStateTextView.setText("正在接听...");
                        EMChatManager.getInstance().answerCall();
                        isAnswered = true;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        saveCallRecord(0);
                        finish();
                        return;
                    }
                }

//			comingBtnContainer.setVisibility(View.INVISIBLE);
                answering_layout.setVisibility(View.INVISIBLE);
                hangupBtn.setVisibility(View.VISIBLE);

                call_layout.setVisibility(View.VISIBLE);
//
// 			voiceContronlLayout.setVisibility(View.VISIBLE);
                answering_layout.setVisibility(View.GONE);
                closeSpeakerOn();
                break;

            case R.id.btn_hangup_call: // 挂断电话
                hangupBtn.setEnabled(false);
                if (soundPool != null)
                    soundPool.stop(streamID);
                chronometer.stop();
                endCallTriggerByMe = true;
                callStateTextView.setText(getResources().getString(
                        R.string.hanging_up));
                try {
                    EMChatManager.getInstance().endCall();
                } catch (Exception e) {
                    e.printStackTrace();
                    saveCallRecord(0);
                    finish();
                }
                break;
            case R.id.iv_mute: // 静音开关
                if (isMuteState) {
                    // 关闭静音
//				muteImage.setImageResource(R.drawable.icon_mute_normal);
                    audioManager.setMicrophoneMute(false);
                    txt_mute.setTextColor(this.getResources().getColor(R.color.white));
                    isMuteState = false;
                    muteImage.setBackgroundResource(R.drawable.mute_up);
                } else {
                    // 打开静音
//				muteImage.setImageResource(R.drawable.icon_mute_on);
                    audioManager.setMicrophoneMute(true);
                    txt_mute.setTextColor(this.getResources().getColor(R.color.text_call_bule));
                    isMuteState = true;
                    muteImage.setBackgroundResource(R.drawable.mute_down);
                }
                break;
            case R.id.iv_handsfree: // 免提开关
                if (isHandsfreeState) {
                    // 关闭免提
                    handsFreeImage.setBackgroundResource(R.drawable.handsfree_up);
                    closeSpeakerOn();
                    txt_handsfree.setTextColor(this.getResources().getColor(R.color.white));
                    isHandsfreeState = false;
                } else {
                    handsFreeImage.setBackgroundResource(R.drawable.handsfree_down);
                    openSpeakerOn();
                    txt_handsfree.setTextColor(this.getResources().getColor(R.color.text_call_bule));
                    isHandsfreeState = true;
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HXSDKHelper.getInstance().isVoiceCalling = false;
    }

    @Override
    public void onBackPressed() {
        EMChatManager.getInstance().endCall();
        callDruationText = chronometer.getText().toString();
        saveCallRecord(0);
        finish();
    }

}
